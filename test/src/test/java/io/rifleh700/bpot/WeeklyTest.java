package io.rifleh700.bpot;

import io.rifleh700.bpot.api.BcweeklyApi;
import io.rifleh700.bpot.api.model.BcweeklyListRs;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeeklyTest {

    private static final Logger log = LoggerFactory.getLogger(WeeklyTest.class);

    private final BcweeklyApi weekly;

    public WeeklyTest() {

        this.weekly = new RestEasyClientProxyFactory()
                .build(BcweeklyApi.class);
    }

    @Test
    public void get() {

        BcweeklyListRs rs = weekly.bcweekly3ListGet(10, 0);
        weekly.bcweekly3GetGet(rs.getResults().get(0).getId());
    }
}
