package ch.gatzka;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Result;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;

public abstract class JOOQRepository<R extends UpdatableRecord<R>, P extends Number> {

    protected final DSLContext dslContext;

    protected final Table<R> table;

    protected final Sequence<P> sequence;

    protected final TableField<R, P> idField;

    protected JOOQRepository(@NotNull DSLContext dslContext, @NotNull Table<R> table, @Nullable Sequence<P> sequence, @Nullable TableField<R, P> idField) {
        this.dslContext = dslContext;
        this.table = table;
        this.sequence = sequence;
        this.idField = idField;
    }

    public P insert(@NotNull Function<R, R> mapping) {
        return dslContext.transactionResult(trx -> {
            DSLContext dslContext = trx.dsl();
            R newRecord = dslContext.newRecord(table);
            P id = null;
            if (sequence != null && idField != null) {
                id = dslContext.nextval(sequence);
                newRecord.set(idField, id);
            }

            dslContext.insertInto(table).set(mapping.apply(newRecord)).execute();

            return id;
        });
    }

    @Nullable
    public R get(@NotNull Condition condition) {
        return dslContext.fetchSingle(table, condition);
    }

    @Nullable
    public R get(@NotNull P id) {
        if (idField == null) {
            throw new IllegalStateException("The table " + table.getName() + " does not have an id column");
        }
        return get(idField.eq(id));
    }

    @NotNull
    public Result<R> read(@NotNull Condition condition) {
        return dslContext.fetch(table, condition);
    }

    @NotNull
    public Result<R> read(@NotNull Condition condition, @NotNull OrderField<?>... orderFields) {
        return dslContext.selectFrom(table).where(condition).orderBy(orderFields).fetch();
    }

    public int count(@NotNull Condition condition) {
        return dslContext.fetchCount(table, condition);
    }

    public boolean exists(@NotNull Condition condition) {
        return dslContext.fetchExists(table, condition);
    }

    public int delete(@NotNull Condition condition) {
        return dslContext.deleteFrom(table).where(condition).execute();
    }

    public void delete(@NotNull P id) {
        if (idField == null) {
            throw new IllegalStateException("The table " + table.getName() + " does not have an id column");
        }
        dslContext.deleteFrom(table).where(idField.eq(id)).execute();
    }

    public <V> int update(@NotNull TableField<R, V> field, V value, @NotNull Condition condition) {
        return dslContext.transactionResult(trx -> trx.dsl().update(table).set(field, value).where(condition).execute());
    }

    public <V> void update(@NotNull TableField<R, V> field, V value, @NotNull P id) {
        if (idField == null) {
            throw new IllegalStateException("The table " + table.getName() + " does not have an id column");
        }
        update(field, value, idField.eq(id));
    }

}
