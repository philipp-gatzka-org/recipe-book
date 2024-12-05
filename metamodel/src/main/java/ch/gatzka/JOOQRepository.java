package ch.gatzka;

import java.util.function.Function;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdateFromStep;
import org.jooq.UpdateSetFirstStep;

public abstract class JOOQRepository<R extends TableRecord<R>, I> {

    protected final DSLContext dslContext;

    protected final Table<R> table;

    protected JOOQRepository(DSLContext dslContext, Table<R> table) {
        this.dslContext = dslContext;
        this.table = table;
    }

    public R get(Condition condition) {
        return dslContext.fetchSingle(table, condition);
    }

    public Result<R> read(Condition condition) {
        return dslContext.fetch(table, condition);
    }

    public Result<R> read(Condition condition, OrderField<?>... orderBy) {
        return dslContext.selectFrom(table).where(condition).orderBy(orderBy).fetch();
    }

    public boolean exists(Condition condition) {
        return dslContext.fetchExists(table, condition);
    }

    public int count(Condition condition) {
        return dslContext.fetchCount(table, condition);
    }

    public int delete(Condition condition) {
        return dslContext.transactionResult(trx -> trx.dsl().deleteFrom(table).where(condition).execute());
    }

    public <V> int update(TableField<R, V> field, V value, Condition condition) {
        return dslContext.transactionResult(trx -> trx.dsl().update(table).set(field, value).where(condition).execute());
    }

    public int update(Function<UpdateSetFirstStep<R>, UpdateFromStep<R>> mapping, Condition condition) {
        return dslContext.transactionResult(trx -> mapping.apply(trx.dsl().update(table)).where(condition).execute());
    }

    public I insert(Function<R, R> mapping) {
        dslContext.transaction(trx -> {
            DSLContext dslContext = trx.dsl();
            R newRecord = dslContext.newRecord(table);
            dslContext.insertInto(table).set(mapping.apply(newRecord)).execute();
        });

        return null;
    }

}
