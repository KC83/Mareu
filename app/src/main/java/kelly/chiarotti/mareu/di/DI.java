package kelly.chiarotti.mareu.di;

import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.DummyApiService;

public class DI {
    private static ApiService service = new DummyApiService();

    /**
     * Get an instance on @{@link DummyApiService}
     * @return
     */
    public static ApiService getApiService() {
        return service;
    }

    public static ApiService getNewInstanceApiService() {
        return new DummyApiService();
    }
}
