package com.example.bookprobyjays.controller;

import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.domain.Warehouse;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.BookService;
import com.example.bookprobyjays.service.WarehouseService;
import com.example.bookprobyjays.vo.WareHouseListVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wareHouse")
@Api(tags = "书籍仓库表")
public class WareHouseController {
    @Resource
    private WarehouseService warehouseService;

    /**
     * 增加仓库书籍信息
     * @param warehouse
     * @return
     */
    @PostMapping("/add/WareHouse")
    public BaseResponse<Warehouse> addWareHouse(@RequestBody Warehouse warehouse){
        if(warehouse.getBookId() == null || warehouse.getBookNum() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"书籍id或数量输入为空");
        }
        return ResultUtils.success(warehouseService.addWarehouseBook(warehouse));
    }

    /**
     * 删除仓库书籍信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/WareHouse/{id}")
    public BaseResponse<Long> deleteWareHouse(@PathVariable("id") Long id){
        if(id == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"书籍id输入为空");
        }
        Warehouse warehouse = warehouseService.getById(id);
        if(warehouse == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"书籍不存在");
        }
        return ResultUtils.success(warehouseService.deleteWarehouseBook(id));
    }

    /**
     * 修改仓库书籍信息
     * @param warehouse
     * @return
     */
    @PutMapping("/update/WareHouse")
    public BaseResponse<Warehouse> updateWareHouse(@RequestBody Warehouse warehouse){
        if(warehouse == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"书籍修改信息为空");
        }
        return ResultUtils.success(warehouseService.updateWarehouseBook(warehouse));
    }

    /**
     * 查看仓库库存，并显示书籍相关信息
     * 注意，这时候是只完成了listVo输出，但是一般要做分页，所有需要后续改进
     * @return
     */
    @GetMapping("/list/WarehouseListVo")
    public BaseResponse<List<WareHouseListVo>> listWarehouseVo(){
        return ResultUtils.success(warehouseService.listWareHouse());
    }
}
