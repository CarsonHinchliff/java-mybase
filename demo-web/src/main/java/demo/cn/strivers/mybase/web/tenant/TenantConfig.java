package demo.cn.strivers.mybase.web.tenant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.jpa.properties.hibernate")
public class TenantConfig {

    private Map<Integer, String> tenantSchema;

    public void setTenantSchema(Map<Integer, String> tenantSchema) {
        this.tenantSchema = tenantSchema;
        Tenant.schemaMap =tenantSchema;
    }
}
