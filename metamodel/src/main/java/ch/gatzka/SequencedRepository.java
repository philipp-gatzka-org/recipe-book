package ch.gatzka;

import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;

public abstract class SequencedRepository<R extends UpdatableRecord<R>, I extends Number> extends JOOQRepository<R, I> {

    protected final Sequence<I> sequence;

    protected final TableField<R, I> sequencedField;

    protected SequencedRepository(DSLContext dslContext, Table<R> table, Sequence<I> sequence, TableField<R, I> sequencedField) {
        super(dslContext, table);
        this.sequence = sequence;
        this.sequencedField = sequencedField;
    }

    @Override
    public I insert(Function<R, R> mapping) {
        return dslContext.transactionResult(trx -> {
            DSLContext dslContext = trx.dsl();

            R newRecord = dslContext.newRecord(table);

            I id = dslContext.nextval(sequence);

            newRecord.set(sequencedField, id);

            dslContext.insertInto(table).set(mapping.apply(newRecord)).execute();

            return id;
        });
    }

    public void delete(I id) {
        delete(sequencedField.eq(id));
    }

    public <V> void update(TableField<R, V> field, V value, I id) {
        update(field, value, sequencedField.eq(id));
    }

}
