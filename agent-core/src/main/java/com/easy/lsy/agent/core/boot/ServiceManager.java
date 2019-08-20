package com.easy.lsy.agent.core.boot;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public enum ServiceManager {
    INSTANCE;
    private static final ILog logger = LogManager.getLogger(ServiceManager.class);

    public void boot() {
        List<IService> allServices = new ArrayList<>();
        loadAllServices(allServices);
        startup(allServices);
    }

    private void startup(List<IService> allServices) {
        for (IService service : allServices) {
            try {
                service.boot();
            } catch (Throwable throwable) {
                logger.error(service.getClass().getName() + "Boot failure", throwable);
            }
        }
    }

    void loadAllServices(List<IService> allServices) {
        Iterator<IService> iterator = ServiceLoader.load(IService.class).iterator();
        while (iterator.hasNext()) {
            allServices.add(iterator.next());
        }
    }
}
