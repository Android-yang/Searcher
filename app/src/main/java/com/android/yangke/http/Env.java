package com.android.yangke.http;

/**
 * 服务器环境
 */
public enum Env {
    UAT {
        @Override
        String apiServerHost() {
            return HttpPar.SERVER_HOST_UAT;
        }

        @Override
        String imageServerHost() {
            return HttpPar.SERVER_HOST_IMAGE_UAT;
        }

        @Override
        String serverSslPinning() {
            return HttpPar.SERVER_HOST_UAT_PIN;
        }

        @Override
        String serverHostName() {
            return HttpPar.SERVER_HOST_UAT_HOSTNAME;
        }

    },
    PRODUCT {
        @Override
        String apiServerHost() {
            return HttpPar.SERVER_HOST_PRODUCT;
        }

        @Override
        String imageServerHost() {
            return HttpPar.SERVER_HOST_IMAGE_PRODUCT;
        }

        @Override
        String serverSslPinning() {
            return HttpPar.SERVER_HOST_PRODUCT_PIN;
        }

        @Override
        String serverHostName() {
            return HttpPar.SERVER_HOST_PRODUCT_HOSTNAME;
        }

    },;

    abstract String apiServerHost();

    abstract String imageServerHost();

    /**
     * https 协议的SSL PINNING
     */
    abstract String serverSslPinning();

    abstract String serverHostName();

}
