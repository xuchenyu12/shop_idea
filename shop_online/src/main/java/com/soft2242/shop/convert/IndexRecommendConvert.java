package com.soft2242.shop.convert;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.soft2242.shop.entity.IndexRecommend;
import com.soft2242.shop.vo.IndexRecommendVO;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface IndexRecommendConvert {
    IndexRecommendConvert INSTANCE = Mappers.getMapper(IndexRecommendConvert.class);

    @Mapping(expression = "java(MapStruct.strToList(indexRecommend.getPictures()))", target = "pictures")
    default IndexRecommendVO convertToIndexRecommendVo(IndexRecommend indexRecommend) {
        return null;
    }

    List<IndexRecommendVO> convertToUserVoList(List<IndexRecommend> list);

    class MapStruct {
        public static List<String> strToList(String str) {
            if (StringUtils.isNotEmpty(str)) {
                return Arrays.asList(str.split(","));
            }
            return null;
        }
    }
}