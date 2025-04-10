package demo.cn.strivers.mybase.web.tenant;

import cn.strivers.mybase.core.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.hikaricp.internal.HikariCPConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
public class TenantConnectionProvider implements MultiTenantConnectionProvider, Stoppable, Configurable {

    private final HikariCPConnectionProvider provider = new HikariCPConnectionProvider();

    @Override
    public Connection getAnyConnection() throws SQLException {
        return provider.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        provider.closeConnection(connection);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        if(StrUtil.isEmpty(tenantIdentifier)){
            log.error("Invalid Tenant Schema: {}",tenantIdentifier);
            throw new HibernateException("Invalid Tenant schema [" + tenantIdentifier + "]");
        }
        final Connection connection = getAnyConnection();
        try {
            connection.createStatement().execute("USE " + tenantIdentifier);
        } catch (SQLException e) {
            log.error("Invalid Tenant Schema: {}",tenantIdentifier);
            throw new HibernateException("Invalid schema [" + tenantIdentifier + "]", e);
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("USE "+ Tenant.defaultSchema);
        } catch (SQLException e) {
            log.error("Invalid Tenant Schema: {}",tenantIdentifier);
            throw new HibernateException("Invalid Tenant schema [" + Tenant.defaultSchema + "]", e);
        }
        provider.closeConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return provider.supportsAggressiveRelease();
    }

    @Override
    public void stop() {
        provider.stop();
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return provider.isUnwrappableAs(aClass);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return provider.unwrap(aClass);
    }

    @Override
    public void configure(Map map) {
        String url=String.valueOf(map.get("hibernate.connection.url"));
        Tenant.defaultSchema = url.substring(0,url.indexOf('?')).substring(url.lastIndexOf('/') + 1);
        provider.configure(map);
    }
}
