package io.rifleh700.bpot;

import io.rifleh700.bpot.api.DiscoverApi;
import io.rifleh700.bpot.api.model.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiscoverTest {

    private static final Logger log = LoggerFactory.getLogger(DiscoverTest.class);

    private final DiscoverApi discover;

    public DiscoverTest() {

        this.discover = new RestEasyClientProxyFactory()
                .build(DiscoverApi.class);
    }

    @Test
    public void web() {

        discover.discover1DiscoverWebPost(new DiscoverWebRq()
                .tagNormNames(List.of())
                .addIncludeResultTypesItem(DiscoverType.A)
                .addIncludeResultTypesItem(DiscoverType.S));
    }

    @Test
    public void tag_arts() {

        discover.discover3DiscoverTagArtsGet("vaporwave");
    }

    @Test
    public void get() {

        discover.discover3GetGet("vaporwave", null, DiscoverSort.REC, DiscoverRec.MOST, Format.ALL, 0L, 1);
    }

    @Test
    public void web_get() {

        discover.discover3GetWebGet("vaporwave", null, DiscoverSort.REC, DiscoverRec.MOST, Format.ALL, 0L, 1);
    }
}
