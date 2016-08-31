package ru.andreymarkelov.interview.infobip.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "urls")
public class Url extends AbstractTimestampedEntity {
    private String originUrl;

    private String shortUrl;

    private Integer count;

    private String accountId;

    private Integer redirectCode;

    public Integer getRedirectCode() {
        return redirectCode;
    }

    public void setRedirectCode(Integer redirectCode) {
        this.redirectCode = redirectCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
