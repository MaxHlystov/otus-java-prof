package ru.fmtk.khlystov.otus_java.core.repository;

import java.util.Arrays;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public final class HibernateUtils {

    private HibernateUtils() {
    }

    public static SessionFactory buildSessionFactory(ServiceRegistry serviceRegistry,
                                                     Configuration configuration,
                                                     Class<?>... annotatedClasses) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Arrays.stream(annotatedClasses).forEach(metadataSources::addAnnotatedClass);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    public static StandardServiceRegistry createServiceRegistry(Configuration configuration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
    }
}
