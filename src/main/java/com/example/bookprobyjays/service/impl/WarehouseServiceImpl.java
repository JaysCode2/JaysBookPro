package com.example.bookprobyjays.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.domain.Warehouse;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.exception.ThrowUtils;
import com.example.bookprobyjays.service.WarehouseService;
import com.example.bookprobyjays.mapper.WarehouseMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiexiang
* @description 针对表【warehouse(存储书籍库存信息的表)】的数据库操作Service实现
* @createDate 2023-12-12 09:24:51
*/
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse>
    implements WarehouseService {

    /**
     * 添加书籍仓库信息
     * @param warehouse
     * @return
     */
    @Override
    public Warehouse addWarehouseBook(Warehouse warehouse) {
        if(this.getById(warehouse.getBookId()) != null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该书籍已存在");
        }
        boolean saveWarehouse = this.save(warehouse);
        ThrowUtils.throwIf(!saveWarehouse, ErrorCode.SYSTEM_ERROR,"添加失败");
        return warehouse;
    }

    /**
     * 删除书籍书籍仓库信息
     * @param id
     * @return
     */
    @Override
    public Long deleteWarehouseBook(Long id) {
        boolean deleteWarehouse = this.removeById(id);
        ThrowUtils.throwIf(!deleteWarehouse, ErrorCode.SYSTEM_ERROR,"删除失败");
        return id;
    }

    /**
     * 修改书籍仓库信息
     * @param warehouse
     * @return
     */
    @Override
    public Warehouse updateWarehouseBook(Warehouse warehouse) {
        Warehouse warehouseExist = this.getById(warehouse);
        if(warehouseExist == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"所要修改的书籍不存在");
        }
        warehouseExist.setBookNum(warehouse.getBookNum());
//        boolean update =  this.save(warehouseExist);
        boolean update =  this.updateById(warehouseExist);
        ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR,"修改失败");
        return warehouseExist;
    }
}




