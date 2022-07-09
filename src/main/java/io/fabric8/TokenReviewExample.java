package io.fabric8;

import io.fabric8.kubernetes.api.model.authentication.TokenReview;
import io.fabric8.kubernetes.api.model.authentication.TokenReviewBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class TokenReviewExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            TokenReview tokenReview = new TokenReviewBuilder()
                    .withNewSpec()
                    .withToken("eyJhbGciOiJSUzI1NiIsImtpZCI6InJmdVBvM28yRE9ZaWpCc2xMYV95QU9fX3VrM01iU2JEY19uWUVUTUZmSHMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJ0dGwtY29udHJvbGxlci10b2tlbi14cjJ4MiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJ0dGwtY29udHJvbGxlciIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImJmOTNmOTg5LTcxZDctNDFiMi1iYTg1LWFkYTg4ZWQwODNlOSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTp0dGwtY29udHJvbGxlciJ9.p9M_FFkNhDGhKfU7J1v4GwQ9Y8hvi1m4oqRUXexgaaImJxTLJUxmRJ7iX3XtpD81EbXR4FWvOk4qfIKX8WgG3MZVk_UZp44ZbFdzJHese_3tmb2fP5dc-f4-bcxDtxdRmS8w7-9uh0lG5-Oks9cOUNj6G7HP6Ky3QMPm4XmWyjDXo8iN-orIKFRRa8RDA6UKgqVmNFiKWNdQCSdJdINSJWVHKXb8BpXAQJmsUgvFk-frtT1DX-0q7K6AJ4vuX1F0VL2AT5Kf4w8kq8JP47_iFeNv8t7Zh3G5pOzaMvSheB5LWMp_Cd-OFdjNhI_uHPHSXxx9I3G3wCZ5U19-2DcDLg")
                    .endSpec()
                    .build();
            tokenReview = client.tokenReviews().create(tokenReview);
            System.out.println(tokenReview.getStatus().getAuthenticated());
        }
    }
}