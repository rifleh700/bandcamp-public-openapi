package io.rifleh700.bpot;

import io.rifleh700.bpot.api.FancollectionApi;
import io.rifleh700.bpot.api.model.FanCollectionRq;
import io.rifleh700.bpot.api.model.FanSearchRq;
import io.rifleh700.bpot.api.model.TralbumType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class FanCollectionTest {

    private static final Logger log = LoggerFactory.getLogger(FanCollectionTest.class);

    private final FancollectionApi fanCollectionApi;

    public FanCollectionTest() {

        this.fanCollectionApi = new RestEasyClientProxyFactory()
                .build(FancollectionApi.class);
    }

    @Test
    public void collectionItems() {

        for (long id : DataPool.FANS)
            fanCollectionApi.fancollection1CollectionItemsPost(new FanCollectionRq()
                    .fanId(id)
                    .count(100)
                    .olderThanToken(new Token(Instant.now(), 0L, TralbumType.A, null).toString()));
    }

    @Test
    public void followers() {

        for (long id : DataPool.FANS)
            fanCollectionApi.fancollection1FollowersPost(new FanCollectionRq()
                    .fanId(id)
                    .count(100)
                    .olderThanToken(new Token(Instant.now(), 0L, TralbumType.A, null).toString()));
    }

    @Test
    public void followingBands() {

        for (long id : DataPool.FANS)
            fanCollectionApi.fancollection1FollowingBandsPost(new FanCollectionRq()
                    .fanId(id)
                    .count(100)
                    .olderThanToken(new Token(Instant.now(), 0L, TralbumType.A, null).toString()));
    }

    @Test
    public void followingFans() {

        for (long id : DataPool.FANS)
            fanCollectionApi.fancollection1FollowingFansPost(new FanCollectionRq()
                    .fanId(id)
                    .count(100)
                    .olderThanToken(new Token(Instant.now(), 0L, TralbumType.A, null).toString()));
    }

    @Test
    public void followingGenres() {

        for (long id : DataPool.FANS)
            fanCollectionApi.fancollection1FollowingGenresPost(new FanCollectionRq()
                    .fanId(id)
                    .count(100)
                    .olderThanToken(new Token(Instant.now(), 0L, TralbumType.A, null).toString()));
    }

    @Test
    public void wishlistItems() {

        for (long id : DataPool.FANS)
            fanCollectionApi.fancollection1WishlistItemsPost(new FanCollectionRq()
                    .fanId(id)
                    .count(100)
                    .olderThanToken(new Token(Instant.now(), 0L, TralbumType.A, null).toString()));
    }

    @Test
    public void searchItems() {

        for (long id : DataPool.FANS)
            fanCollectionApi.fancollection1SearchItemsPost(new FanSearchRq()
                    .fanId(id)
                            .searchKey("a")
                            .searchType(FanSearchRq.SearchTypeEnum.COLLECTION));
    }
}
