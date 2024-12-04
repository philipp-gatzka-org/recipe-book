package ch.gatzka.service.repository;

import ch.gatzka.SequencedRepository;
import ch.gatzka.tables.records.AccountRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static ch.gatzka.Sequences.S_ACCOUNT_ID;
import static ch.gatzka.Tables.ACCOUNT;

@Repository
public class AccountRepository extends SequencedRepository<AccountRecord, Integer> {

    public AccountRepository(DSLContext dslContext) {
        super(dslContext, ACCOUNT, S_ACCOUNT_ID, ACCOUNT.ID);
    }

    public boolean existsByEmail(String email) {
        return exists(ACCOUNT.EMAIL.eq(email));
    }

}
