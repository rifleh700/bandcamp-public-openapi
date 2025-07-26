package io.rifleh700.bpot;

import io.rifleh700.bpot.api.BcweeklyApi;
import io.rifleh700.bpot.api.model.BcweeklyListRs;
import org.junit.Test;

public class WeeklyTest {

    private final BcweeklyApi weekly;

    public WeeklyTest() {

        this.weekly = new RestEasyClientProxyFactory()
                .build(BcweeklyApi.class);
    }

    @Test
    public void get() {

        BcweeklyListRs rs = weekly.bcweekly3ListGet(1000, 0);
        weekly.bcweekly3GetGet(rs.getResults().get(0).getId());
    }
}
