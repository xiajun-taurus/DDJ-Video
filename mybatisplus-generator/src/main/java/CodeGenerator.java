import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xiajun
 */
public class CodeGenerator {

    public static final String URL = "jdbc:mysql://localhost:3306/douyin?useUnicode=true&useSSL=false" +
            "&characterEncoding=utf8";
    public static final String USER_NAME = "root";

    public static final String PASSWORD = "123456";

    public static final String OUT_PUT_DIR = "classpath:/";

    public static final String PARENT_PACKAGE_NAME = "tech.xiajun";

    public static final List<String> TABLE_NAMES = new ArrayList<>();

    static {
        TABLE_NAMES.add("bgm");
        TABLE_NAMES.add("comments");
        TABLE_NAMES.add("search_records");
        TABLE_NAMES.add("users");
        TABLE_NAMES.add("users_fans");
        TABLE_NAMES.add("users_like_videos");
        TABLE_NAMES.add("users_reports");
        TABLE_NAMES.add("videos");
    }
    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USER_NAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author("xiajun") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(OUT_PUT_DIR); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT_PACKAGE_NAME) // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, OUT_PUT_DIR + "/xml")); // 设置mapperXml
                    // 生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAMES); // 设置需要生成的表名
                })
                .execute();


    }

}
