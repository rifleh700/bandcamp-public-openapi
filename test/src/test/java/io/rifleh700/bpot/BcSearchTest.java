package io.rifleh700.bpot;

import io.rifleh700.bpot.api.BcsearchPublicApiApi;
import io.rifleh700.bpot.api.model.AcElasticRq;
import io.rifleh700.bpot.api.model.ItemType;
import io.rifleh700.bpot.api.model.PrefixRq;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcSearchTest {

    private static final Logger log = LoggerFactory.getLogger(BcSearchTest.class);

    private final BcsearchPublicApiApi search;

    public BcSearchTest() {

        this.search = new RestEasyClientProxyFactory()
                .build(BcsearchPublicApiApi.class);
    }

    @Test
    public void ac() {

        search.bcsearchPublicApi1AutocompleteElasticPost(
                new AcElasticRq()
                        .searchText("vapor")
                        .searchFilter(ItemType.EMPTY)
                        .fanId(0L)
                        .fullPage(true));
    }

    @Test
    public void ac_fan() {

        for (long fanId : DataPool.FANS)
            search.bcsearchPublicApi1AutocompleteElasticPost(
                    new AcElasticRq()
                            .searchText("vapor")
                            .searchFilter(ItemType.EMPTY)
                            .fanId(fanId)
                            .fullPage(true));
    }

    @Test
    public void tag() {

        search.bcsearchPublicApi1TagSearchPost(
                new PrefixRq()
                        .prefix("vapor"));
    }
}
