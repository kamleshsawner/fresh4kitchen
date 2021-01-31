package singa.tech.fresh4kitchen.util;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.model.Varient;

public class ProjectUtility {

    public static ArrayList<String> getBannerList(Products product) {
        ArrayList<String> banner_list = new ArrayList<>();
        if (!product.getProduct_img1().equals(Webapi.BASE_IMAGE_URL)) {
            banner_list.add(product.getProduct_img1());
        }
        if (!product.getProduct_img2().equals(Webapi.BASE_IMAGE_URL)) {
            banner_list.add(product.getProduct_img2());
        }
        if (!product.getProduct_img3().equals(Webapi.BASE_IMAGE_URL)) {
            banner_list.add(product.getProduct_img3());
        }
        if (!product.getProduct_img4().equals(Webapi.BASE_IMAGE_URL)) {
            banner_list.add(product.getProduct_img4());
        }
        return banner_list;
    }

    public static ArrayList<Varient> getVarientList(Products products) {

        final String[] offerPrice = products.getOffer_price().split(",");
        final String[] actualPrice = products.getPrice().split(",");
        final String[] Weight = products.getUnit().split(",");
        final String[] Percentage = products.getPercentage().split(",");
        final String[] Varient_id = products.getVerient_id().split(",");

        ArrayList<Varient> list = new ArrayList<>();
        for (int i = 0; i < offerPrice.length; i++) {
            list.add(new Varient("", products.getId(), Varient_id[i], offerPrice[i],
                    actualPrice[i], Weight[i], Percentage[i]));
        }
        return list;
    }
}
