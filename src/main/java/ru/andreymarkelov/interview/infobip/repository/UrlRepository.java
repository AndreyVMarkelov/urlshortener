package ru.andreymarkelov.interview.infobip.repository;

import org.springframework.data.repository.CrudRepository;
import ru.andreymarkelov.interview.infobip.domain.Url;

import java.util.List;

public interface UrlRepository extends CrudRepository<Url, Long> {
    Url findByAccountIdAndOriginUrl(String accountId, String originUrl);
    List<Url> findByAccountId(String accountId);
    Url findByShortUrl(String shortUrl);
}
