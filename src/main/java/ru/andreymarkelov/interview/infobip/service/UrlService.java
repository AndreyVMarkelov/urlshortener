package ru.andreymarkelov.interview.infobip.service;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.andreymarkelov.interview.infobip.domain.Url;
import ru.andreymarkelov.interview.infobip.repository.UrlRepository;
import ru.andreymarkelov.interview.infobip.util.AppException;
import ru.andreymarkelov.interview.infobip.util.UrlNotFoundException;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ru.andreymarkelov.interview.infobip.util.DatabaseUtil.isDatabaseConstraintViolationException;

@Service
public class UrlService {
    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private UrlRepository urlRepository;

    @Transactional
    public Url getAndUpdateCount(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url == null) {
            throw new UrlNotFoundException(shortUrl);
        }
        url.setCount(url.getCount() + 1);
        urlRepository.save(url);
        return url;
    }

    @Transactional
    public List<Url> getUrlsByAccountId(String accountId) {
        return urlRepository.findByAccountId(accountId);
    }

    @Transactional
    public Url createUrl(String accountId, String originUrl, int redirectCode) {
        Url existingUrl = urlRepository.findByAccountIdAndOriginUrl(accountId, originUrl);
        if (existingUrl != null) {
            return existingUrl;
        }

        Url url = new Url();
        url.setOriginUrl(originUrl);
        url.setCount(0);
        url.setAccountId(accountId);
        url.setRedirectCode(redirectCode);

        String shortUrl = shortUrl(originUrl);
        int count = 0;
        while (true) {
            try {
                url.setShortUrl(count == 0 ? shortUrl : shortUrl + count);
                return urlRepository.save(url);
            } catch (PersistenceException ex) {
                if (isDatabaseConstraintViolationException(ex)) {
                    count++;
                } else {
                    throw ex;
                }
            }
        }
    }

    private String shortUrl(String url) {
        try {
            URL urlObj = new URL(url);
            String localhost = InetAddress.getLocalHost().getHostName().toLowerCase();
            String id = Hashing.murmur3_32().hashString(urlObj.getPath(), StandardCharsets.UTF_8).toString();
            return new URL("http", localhost, serverPort, "/r/" + id).toString();
        } catch (Exception ex) {
            throw new AppException("Cannot shortify URL", ex);
        }
    }
}
