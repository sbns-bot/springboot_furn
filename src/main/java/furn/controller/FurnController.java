package furn.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import furn.bean.Furn;
import furn.service.FurnService;
import furn.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j//输出日志
public class FurnController {

    @Resource
    private FurnService furnService;

    //前端以json格式发请求，@RequestBody将json请求封装成javaBean对象
    //如果是以表单形式提交，则不需要@RequestBody
    @PostMapping("/save")
    public Result save(@Validated @RequestBody Furn furn, Errors errors) {

        //如果出现校验错误, springboot 底层会把错误信息，封装到errors

        //定义map ,准备把errors中的校验错误放入到map,如果有错误信息
        //就不真正添加，并且将错误信息通过map返回给客户端-客户端就可以取出显示
        HashMap<String, Object> map = new HashMap<>();

        List<FieldError> fieldErrors = errors.getFieldErrors();
        //遍历 将错误信息放入到map , 当然可能有，也可能没有错误
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        if (map.isEmpty()) { //说明没有校验错误,正常添加
            log.info("furn={}", furn);
            furnService.save(furn);
            return Result.success(); //返回成功信息
        } else {
            return Result.error("400", "后端校验失败~", map);
        }

    }


    @ResponseBody
    @RequestMapping("/furns")
    public Result listFurns() {
        List<Furn> furnList = furnService.list();
        return Result.success(furnList);
    }


    @PutMapping("/update")
    public Result update(@RequestBody Furn furn){
        furnService.updateById(furn);
        return Result.success();
    }


    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Integer id) {
        //说明removeById 是Mybatis-Plus提供
        furnService.removeById(id);
        return Result.success();
    }


    //分页查询的接口/方法
    /**
     * @param pageNum  显示第几页 ,默认1
     * @param pageSize 每页显示几条记录 , 默认5
     * @return
     */
    @GetMapping("/furnsByPage")
    public Result listFurnsByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "5") Integer pageSize) {

        //这里通过page方法，返回Page对象, 对象中就封装了分页数据
        Page<Furn> page = furnService.page(new Page<>(pageNum, pageSize));
        //这里我们注意观察，返回的page数据结构是如何的?这样你才能指定在前端如何绑定返回的数据
        return Result.success(page);
    }


    //方法: 可以支持带条件的分页检索
    /**
     * @param pageNum  显示第几页
     * @param pageSize 每页显示几条记录
     * @param search   检索条件: 家居名 , 默认是“”, 表示不带条件检索，正常分页
     * @return
     */
    @GetMapping("/furnsBySearchPage")
    public Result listFurnsByConditionPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "") String search) {

        //先创建QueryWrapper, 可以将我们的检索条件封装到QueryWrapper
        QueryWrapper<Furn> queryWrapper = Wrappers.query();
        //判断search 是否有内容
        if (StringUtils.hasText(search)) {
            queryWrapper.like("name", search);
        }

        Page<Furn> page = furnService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return Result.success(page);
    }
}
