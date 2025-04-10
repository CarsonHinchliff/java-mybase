package demo.cn.strivers.mybase.web.tenant;

import cn.strivers.mybase.core.utils.StrUtil;
import cn.strivers.mybase.web.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

@Slf4j
public class TenantResolver implements CurrentTenantIdentifierResolver {

    /**
     * 切换租户标识
     * @return ：null抛异常 ""正常返回
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        if(Tenant.schemaMap==null){
            log.error("Invalid Tenant Config");
            return null;
        }
        int tenantId = LoginHelper.getLogin().getTenantId();
        String schema=Tenant.schemaMap.get(tenantId);
        if(StrUtil.isEmpty(schema)){
            return "";
        }
        return schema;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
