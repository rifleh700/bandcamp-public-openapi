package io.rifleh700.bpot;

import io.rifleh700.bpot.api.HubApi;
import io.rifleh700.bpot.api.model.DigDeeperRq;
import io.rifleh700.bpot.api.model.DigDeeperRqFilters;
import io.rifleh700.bpot.api.model.Format;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HubTest {

    private static final Logger log = LoggerFactory.getLogger(HubTest.class);

    private final HubApi hub;

    public HubTest() {

        this.hub = new RestEasyClientProxyFactory()
                .build(HubApi.class);
    }

    @Test
    public void digDeeper() {

        hub.hub2DigDeeperPost(new DigDeeperRq()
                .filters(new DigDeeperRqFilters()
                        .addTagsItem("vaporwave")
                        .addTagsItem("future-funk")
                        .format(Format.ALL)
                        .sort(DigDeeperRqFilters.SortEnum.POP)
                        .location(0L))
                .page(1));
    }
}
