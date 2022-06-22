package com.yaoxiong.retail.commodity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.commodity.service.CommodityService;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.vo.CommodityQueryVo;
import com.yaoxiong.retail.vo.CommodityWithOrder;
import com.yaoxiong.retail.vo.CommodityWithOrderQueryVo;
import com.yaoxiong.retail.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/commodity")
@Slf4j
public class CommodityController {

    @Autowired
    private CommodityService commodityService;


    @GetMapping("/withorder/{page}/{pageSize}")
    public Result getCommodityWithOrder(
                                        @PathVariable Long page,
                                        @PathVariable Long pageSize,
                                        CommodityWithOrderQueryVo commodityWithOrderQueryVo) {
        Page<CommodityWithOrder> pageParam = new Page<>(page, pageSize);
        return commodityService.getCommodityWithOrder(pageParam, commodityWithOrderQueryVo);
    }


    @Value("${file.uploadFolder}")
    private String realBasePath;
    @Value("${file.accessPath}")
    private String accessPath;

    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       @Validated CommodityQueryVo commodityQueryVo) {
        Page<Commodity> pageParam = new Page<>(page, limit);
        return commodityService.selectCommodityPage(pageParam, commodityQueryVo);
    }

    @PostMapping()
    public Result createCommodity(@Validated @RequestBody Commodity commodity) {
        return commodityService.saveCommodity(commodity);
    }

    @PutMapping
    public Result updateCommodity(@Validated @RequestBody Commodity commodity) {
        return commodityService.updataCommodity(commodity);
    }


    @GetMapping("/{id}")
    public Result list(@PathVariable Integer id) {
        return commodityService.getCommodityById(id);
    }


    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadImage(@RequestPart("file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()) {
            return Result.error();
        } else {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));

            fileName = UUID.randomUUID().toString() + suffixName;
            Date todayDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(todayDate);

            String saveToPath = accessPath + today + "/";

            String realPath = realBasePath + today + "/";

            String filepath = realPath + fileName;
            log.info("Upload picture named：" + fileName + "--Virtual file path is：" + saveToPath + "--Physical file path is：" + realPath);

            File destFile = new File(filepath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            try {
                file.transferTo(destFile);
                return Result.ok().data("url", saveToPath + fileName);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error().message("upload fail");
            }
        }
    }

    @PutMapping("/inner/updateCommodityAndPurchaseRemain")
    public Result updateCommodityAndPurchaseRemain(@Validated @RequestBody OrderVo orderVo) {
        try {
            return commodityService.updateCommodityAndPurchaseRemain(orderVo);
        } catch (Exception e) {
            return Result.error().message("update remain fail");
        }
    }

}
