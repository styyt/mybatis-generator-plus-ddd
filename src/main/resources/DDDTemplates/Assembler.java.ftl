package ${cfg.assemblerPackage};

import ${cfg.DTOPackAge}.${entity}Dto;
import ${cfg.domainOnePackage}.entity.${entity};
import ${cfg.infPackage}.util.DomainDtoTransUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * @描述: 组装器，dto与聚合之间的相互转换，并添加与业务无关的信息,如时间，id
 * @ps： 可用Java实体映射工具MapStruct
 * @公司: lumi
 * @author ${author}
 * @since ${date}
 */
@Component
public class Assembler {

    /**
    * @描述: 简单的展示了dto和domain的转换，其他情况请自己补充
    */
    public ${entity} insertDtoTo${entity} (${entity}Dto dto) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (dto == null) {
            return null;
        }
        ${entity} obj=new ${entity}();
        return (${entity}) DomainDtoTransUtils.CloneAttribute((Object)dto,(Object)obj);
    }

}
