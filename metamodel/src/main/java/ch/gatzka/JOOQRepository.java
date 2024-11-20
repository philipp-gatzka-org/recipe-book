package ch.gatzka;

import org.jooq.*;

import java.util.function.Function;

public abstract class JOOQRepository<R extends UpdatableRecord<R>, P extends Number> {

    private final DSLContext dslContext;

    private final Table<R> table;

    private final TableField<R, P> primaryKey;

    private final Sequence<P> sequence;

    public JOOQRepository(DSLContext dslContext, Table<R> table, TableField<R, P> primaryKey, Sequence<P> sequence) {
        this.dslContext = dslContext;
        this.table = table;
        this.primaryKey = primaryKey;
        this.sequence = sequence;
    }

    public R get(Condition condition) {
        return dslContext.fetchSingle(table, condition);
    }

    public R get(P id) {
        return get(primaryKey.eq(id));
    }

    public Result<R> read(Condition condition) {
        return read(condition, primaryKey);
    }

    public Result<R> read() {
        return dslContext.selectFrom(table).fetch();
    }

    public Cursor<R> readLazy(Condition condition) {
        return readLazy(condition, primaryKey);
    }

    public Result<R> read(Condition condition, OrderField<?>... orderBy) {
        return dslContext.selectFrom(table).where(condition).orderBy(orderBy).fetch();
    }

    public Cursor<R> readLazy(Condition condition, OrderField<?>... orderBy) {
        return dslContext.selectFrom(table).where(condition).orderBy(orderBy).fetchLazy();
    }

    public boolean exists(Condition condition) {
        return dslContext.fetchExists(table, condition);
    }

    public int count(Condition condition) {
        return dslContext.fetchCount(table, condition);
    }

    public P insert(Function<R, R> mapping) {
        return dslContext.transactionResult(trx -> {
            DSLContext dslContext = trx.dsl();
            P id = dslContext.nextval(sequence);
            R newRecord = dslContext.newRecord(table);
            newRecord.set(primaryKey, id);
            dslContext.insertInto(table).set(mapping.apply(newRecord)).execute();
            return id;
        });
    }

    public int update(Function<R, R> mapping, Condition condition) {
        return dslContext.transactionResult(trx -> {
            int count = 0;
            DSLContext dslContext = trx.dsl();
            try (Cursor<R> cursor = readLazy(condition)) {
                for (R currentRecord : cursor) {
                    dslContext.update(table).set(mapping.apply(currentRecord)).where(condition).execute();
                    count++;
                }
            }
            return count;
        });
    }

    public int update(Function<R, R> mapping, P id) {
        return update(mapping, primaryKey.eq(id));
    }

    public <V> int update(TableField<R, V> field, V value, Condition condition) {
        return dslContext.transactionResult(trx -> {
            DSLContext dslContext = trx.dsl();
            return dslContext.update(table).set(field, value).where(condition).execute();
        });
    }

    public <V> int update(TableField<R, V> field, V value, P id) {
        return update(field, value, primaryKey.eq(id));
    }

    public int delete(Condition condition) {
        return dslContext.transactionResult(trx -> {
            DSLContext dslContext = trx.dsl();
            return dslContext.deleteFrom(table).where(condition).execute();
        });
    }

    public void delete(P id) {
        delete(primaryKey.eq(id));
    }

}
