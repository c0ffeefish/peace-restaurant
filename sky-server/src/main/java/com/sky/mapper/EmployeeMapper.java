package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("SELECT * FROM employee WHERE username = #{username}")
    Employee getByUsername(String username);

    @Insert("INSERT INTO employee (username, name, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "VALUES" +
            "(#{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);


/*
    @Select("SELECT * FROM employee WHERE name = #{name}")
    List<Employee> selectEmployeeByName(String name);

    @Select("SELECT * FROM employee LIMIT #{start}, #{pageSize}")
    List<Employee> selectEmployeePage(Integer start, Integer pageSize);
    */

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> selectEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工
     * @param employee
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 查询指定id员工
     * @param id
     * @return
     */
    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee selectEmployeeById(Long id);
}
