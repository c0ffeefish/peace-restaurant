package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //Done 将前端发送过来的密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 增加员工
     */
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        /**
         * 对象属性拷贝
         * 将employeeDTO中的数据拷贝到employee中
         */
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insertEmployee(employee);
    }

/*
    @Override
    public List<EmployeeDTO> getEmployee(String name, Integer page, Integer pageSize) {
        List<Employee> employeeList = new ArrayList<>();

        if(name != null && !"".equals(name)){
            employeeList = employeeMapper.selectEmployeeByName(name);
        }else{
            Integer start = (page - 1) * pageSize;
            employeeList= employeeMapper.selectEmployeePage(start, pageSize);
        }

        // DONE 将 Employee 列表转换成 EmployeeDTO 列表
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        for(Employee employee : employeeList){
            EmployeeDTO employeedto = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeedto);
            employeeDTOList.add(employeedto);
        }

        return employeeDTOList;
    }*/

    @Override
    public PageResult getEmployeePage(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.selectEmployeePage(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> employeeList = page.getResult();
        List<EmployeeDTO> records = new ArrayList<>();

        for(Employee employee : employeeList) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTO);
            records.add(employeeDTO);
        }

        return new PageResult(total, records);
    }
}
