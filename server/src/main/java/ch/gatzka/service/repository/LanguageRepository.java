package ch.gatzka.service.repository;

import ch.gatzka.SequencedRepository;
import ch.gatzka.tables.records.LanguageRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static ch.gatzka.Sequences.S_LANGUAGE_ID;
import static ch.gatzka.Tables.LANGUAGE;

@Repository
public class LanguageRepository extends SequencedRepository<LanguageRecord, Integer> {

    public LanguageRepository(DSLContext dslContext) {
        super(dslContext, LANGUAGE, S_LANGUAGE_ID, LANGUAGE.ID);
    }

    public boolean existsByCode(String code) {
        return exists(LANGUAGE.CODE.eq(code));
    }

    public LanguageRecord getByCode(String code) {
        return get(LANGUAGE.CODE.eq(code));
    }
}
