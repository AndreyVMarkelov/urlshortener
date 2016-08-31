create table account (
    id bigint auto_increment,
    accountId varchar not null,
    password varchar not null,
    createdDate timestamp not null,
    lastModifiedDate timestamp not null,
    primary key (id),
    unique (accountId)
);

create index accountIdInx on account (accountId);

create table urls (
    id bigint auto_increment,
    originUrl varchar not null,
    shortUrl varchar not null,
    accountId varchar not null,
    count int not null,
    redirectCode int not null,
    createdDate timestamp not null,
    lastModifiedDate timestamp not null,
    primary key (id),
    unique (shortUrl)
);

create unique index urlAccountIdOriginInx on urls (accountId, originUrl);
create unique index urlAccountIdShortInx on urls (shortUrl);
