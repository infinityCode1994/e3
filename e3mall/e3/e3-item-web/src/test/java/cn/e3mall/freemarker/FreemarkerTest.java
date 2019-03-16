package cn.e3mall.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class FreemarkerTest {
    @Test
    public void genFile() throws Exception {
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 第二步：设置模板文件所在的路径。
        configuration.setDirectoryForTemplateLoading(new File("E:/e3/e3-item-web/src/main/webapp/WEB-INF/ftl"));
        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate("hello.ftl");
        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        Map dataModel = new HashMap<>();
        //向数据集中添加数据
        dataModel.put("hello", "this is my first freemarker test.");
        Student student = new Student(1,"xiao梦",18,"很多事");
        dataModel.put("student",student);
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
        Writer out = new FileWriter(new File("F:/temp/freemarker/hello.html"));
        // 第七步：调用模板对象的process方法输出文件。
        template.process(dataModel, out);
        // 第八步：关闭流。
        out.close();
    }
    @Test
    public void genStudentFile() throws Exception {
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 第二步：设置模板文件所在的路径。
        configuration.setDirectoryForTemplateLoading(new File("E:/e3/e3-item-web/src/main/webapp/WEB-INF/ftl"));
        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate("student.ftl");
        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        Map dataModel = new HashMap<>();
        //向数据集中添加数据
        Student student = new Student(1,"xiao梦",18,"很多事");
        dataModel.put("student",student);
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"恢复大师",18,"field"));
        studentList.add(new Student(2,"GV第三个人",18,"法国人"));
        studentList.add(new Student(3,"恢复大师4",18,"field"));
        studentList.add(new Student(4,"恢复大师65",18,"field"));
        studentList.add(new Student(5,"恢复大师6",18,"field"));
        studentList.add(new Student(6,"恢复大师87",18,"field"));
        studentList.add(new Student(8,"恢复大师79",18,"field"));
        dataModel.put("studentList",studentList);
        dataModel.put("date",new Date());
        dataModel.put("val",null);
        dataModel.put("hello", "this is my first freemarker test.");
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
        Writer out = new FileWriter(new File("F:/temp/freemarker/student.html"));
        // 第七步：调用模板对象的process方法输出文件。
        template.process(dataModel, out);
        // 第八步：关闭流。
        out.close();
    }

}
