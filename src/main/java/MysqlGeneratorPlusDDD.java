import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <p>
 * MysqlGenerator 升级版 plus DDD
 * @author yangyuting
 * @since 2019年11月8日
 * </p>
 */
public class MysqlGeneratorPlusDDD {

    //参数设置
    /**
     * 生成代码类型配置 1、生成普通MVC代码；else 、DDD模型
     */
    private static String type="2";
    /**
     * 模块名称
     */
    private static String moduleName="tools";
    /**
     * 表名
     */
    private static String tableName="tools_demo";

    /**
     * 数据库相关配置
     */
    private static String URL="jdbc:mysql://10.0.14.11:3306/demo?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
    private static String DRIVE="com.mysql.jdbc.Driver";
    private static String USERNAME="demoName";
    private static String PASSWORD="demoPassword";
    /**
     * 父包
     */
    private static String parentPackage="com.demo";
    /**
     * 父路径
     */
    private static String parentPath="com/demo/";

    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/mybatis-generator-plus-DDD/src/main/java");
        gc.setAuthor("styyt");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(URL);
        // dsc.setSchemaName("public");
        dsc.setDriverName(DRIVE);
        dsc.setUsername(USERNAME);
        dsc.setPassword(PASSWORD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(moduleName);
        pc.setParent(parentPackage);
        mpg.setPackageInfo(pc);

        // 自定义配置，生成DDD模型代码
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                //自定义参数
                //参见：com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine.batchOutput
                Map<String,Object> map=new HashMap();
                String apiPackage=parentPackage+"."+moduleName+".api";
                String infPackage=parentPackage+"."+moduleName+".infrastructure";
                //com.lumi.retail.tools.domain.aggregate.tools.entity
                String domainPackage=parentPackage+"."+moduleName+".domain.aggregate";
                String domainOnePackage=domainPackage+"."+moduleName;

                String assemblerPackageName=apiPackage+".assembler";
                String DDDControllerPackAge=apiPackage+".controller";
                String DTOPackAge=apiPackage+".dto";

                map.put("apiPackage",apiPackage);
                map.put("assemblerPackage",assemblerPackageName);
                map.put("DDDControllerPackAge",DDDControllerPackAge);
                map.put("DTOPackAge",DTOPackAge);
                map.put("domainPackage",domainPackage);
                map.put("domainOnePackage",domainOnePackage);
                map.put("infPackage",infPackage);

                setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();

        if("1".equals(type)){
            //MVC模型
            focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return projectPath + "/mybatis-generator-plus-DDD/src/main/resources/sqlMap/" + pc.getModuleName()
                            + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
        }else{
            focList.add(new FileOutConfig("/DDDTemplates/DDDmapper.xml.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return projectPath + "/mybatis-generator-plus-DDD/src/main/resources/sqlMap/" + pc.getModuleName()
                            + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
            //补充剩余的DDD文件
            fillOthersFiles(focList,projectPath,pc);
        }

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("com.lumi.retail.base.common..common.BaseEntity");
        strategy.setEntityLombokModel(true);
        //strategy.setSuperControllerClass("com.lumi.retail.base.common.controller.BaseController");
        strategy.setInclude(tableName);
        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        //加载free marker引擎
        if("1".equals(type)){
            //MVC模型
            mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        }else{
            //非mvc模型时，不生成其下代码
            mpg.setTemplateEngine(new FreemarkerTemplateEngineForDDD(projectPath));
        }
        mpg.execute();


    }

    private static void fillOthersFiles(List<FileOutConfig> focList, String projectPath, PackageConfig pc) {
        focList.add(new FileOutConfig("/DDDTemplates/DDDmapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/resources/sqlMap/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        focList.add(new FileOutConfig("/DDDTemplates/Assembler.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/api/assembler/Assembler"+StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/DDDTemplates/DDDcontroller.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/api/controller/"+tableInfo.getEntityName()+"Controller"+StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/DDDTemplates/entityDto.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/api/dto/"+tableInfo.getEntityName()+"Dto"+StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/DDDTemplates/DDDentity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/domain/aggregate/"+moduleName+"/entity/"+tableInfo.getEntityName()+StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/DDDmapper.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/domain/aggregate/"+moduleName+"/mapper/"+tableInfo.getEntityName()+"Mapper"+StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/blackFile.txt.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/domain/aggregate/"+moduleName+"/dao/blackFile.txt";
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/blackFile.txt.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/domain/aggregate/"+moduleName+"/values/blackFile.txt";
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/blackFile.txt.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/domain/aggregate/"+moduleName+"/repository/blackFile.txt";
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/blackFile.txt.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/infrastructure/consts/blackFile.txt";
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/blackFile.txt.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/infrastructure/feign/blackFile.txt";
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/DomainDtoTransUtils.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/infrastructure/util/DomainDtoTransUtils.java";
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/blackFile.txt.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/infrastructure/config/blackFile.txt";
            }
        });
        focList.add(new FileOutConfig("/DDDTemplates/blackFile.txt.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/mybatis-generator-plus-DDD/src/main/java/"+parentPath + pc.getModuleName()
                        + "/service/xlljob/blackFile.txt";
            }
        });
    }

}
//单独为DDD开发的引擎
class FreemarkerTemplateEngineForDDD extends AbstractTemplateEngine {
    private Configuration configuration;
    private String path;
    public FreemarkerTemplateEngineForDDD() {
    }

    public FreemarkerTemplateEngineForDDD(String path) {
        this.path=path;
    }
    public FreemarkerTemplateEngineForDDD init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        String XMLpath=configBuilder.getPathInfo().get("xml_path");
        configBuilder.getPathInfo().clear();
        configBuilder.getPathInfo().put("xml_path",this.path);
        this.configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        this.configuration.setDefaultEncoding(ConstVal.UTF8);
        this.configuration.setClassForTemplateLoading(com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine.class, "/");
        return this;
    }

    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if("null.ftl".equals(templatePath)){
            return;
        }
        Template template = this.configuration.getTemplate(templatePath);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        Throwable var6 = null;

        try {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
        } catch (Throwable var15) {
            var6 = var15;
            throw var15;
        } finally {
            if (fileOutputStream != null) {
                if (var6 != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable var14) {
                        var6.addSuppressed(var14);
                    }
                } else {
                    fileOutputStream.close();
                }
            }
        }
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }
}
