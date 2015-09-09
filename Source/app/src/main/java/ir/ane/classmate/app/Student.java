package ir.ane.classmate.app;

import com.orm.SugarRecord;

/**
 * Created by hp on 8/29/15.
 */
public class Student extends SugarRecord<Student> {

    public String name;
    public String time;
    public Student()
    {

    }

    public Student(String name,String time)
    {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
