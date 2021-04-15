package cn.eric.h2.interview.delegate;

/**
 * @ClassName EmployeeA
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/15
 * @Version V1.0
 **/
public class EmployeeA implements IEmployee {
    @Override
    public void work(String command) {
        System.out.println("我是员工A，我正在工作:" + command);
    }
}
