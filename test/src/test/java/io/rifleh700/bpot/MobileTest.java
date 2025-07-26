package io.rifleh700.bpot;

import io.rifleh700.bpot.api.MobileApi;
import io.rifleh700.bpot.api.model.BandDetails;
import io.rifleh700.bpot.api.model.DiscographyItem;
import io.rifleh700.bpot.api.model.DiscographyItemType;
import io.rifleh700.bpot.api.model.TralbumType;
import org.junit.Test;

import java.util.stream.Collectors;

public class MobileTest {

    private final MobileApi mobileApi;

    public MobileTest() {

        this.mobileApi = new RestEasyClientProxyFactory()
                .build(MobileApi.class);
    }

    @Test
    public void bandDetails() {

        for (long id : DataPool.BANDS)
            mobileApi.mobile24BandDetailsGet(id);
    }

    @Test
    public void currencies() {

        mobileApi.mobile24CurrenciesGet();
    }

    @Test
    public void fanBio() {

        for (long id : DataPool.FANS)
            mobileApi.mobile24FanBioGet(id);
    }

    @Test
    public void fanCollection() {

        for (long id : DataPool.FANS)
            mobileApi.mobile24FanCollectionGet(id, null);
    }

    @Test
    public void fanFollowedBy() {

        for (long id : DataPool.FANS)
            mobileApi.mobile24FanFollowedByGet(id, null);
    }

    @Test
    public void fanFollows() {

        for (long id : DataPool.FANS)
            mobileApi.mobile24FanFollowsGet(id, null);
    }

    @Test
    public void fanWishlist() {

        for (long id : DataPool.FANS)
            mobileApi.mobile24FanWishlistGet(id, null);
    }

    @Test
    public void merchDetails() {

        for (long id : DataPool.BANDS) {

            BandDetails details = mobileApi.mobile24BandDetailsGet(id);
            String packageIds = details.getMerch().stream().map(v -> String.valueOf(v.getId())).collect(Collectors.joining(","));
            mobileApi.mobile24MerchDetailsGet(id, packageIds);
        }
    }

    @Test
    public void tralbumDetails() {

        for (long id : DataPool.BANDS) {

            DiscographyItem item = mobileApi.mobile24BandDetailsGet(id).getDiscography().get(0);
            mobileApi.mobile24TralbumDetailsGet(
                    id,
                    item.getItemId(),
                    item.getItemType() == DiscographyItemType.ALBUM ?
                            TralbumType.A : TralbumType.T);
        }
    }

    @Test
    public void tralbumLyrics() {

        for (long id : DataPool.BANDS) {

            DiscographyItem item = mobileApi.mobile24BandDetailsGet(id).getDiscography().get(0);
            mobileApi.mobile24TralbumLyricsGet(
                    item.getItemId(),
                    item.getItemType() == DiscographyItemType.ALBUM ?
                            TralbumType.A : TralbumType.T);
        }
    }

    @Test
    public void tralbumTags() {

        for (long id : DataPool.BANDS) {

            DiscographyItem item = mobileApi.mobile24BandDetailsGet(id).getDiscography().get(0);
            mobileApi.mobile24TralbumTagsGet(
                    id,
                    item.getItemId(),
                    item.getItemType() == DiscographyItemType.ALBUM ?
                            TralbumType.A : TralbumType.T);
        }
    }
}
