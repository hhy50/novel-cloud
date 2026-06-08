package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.HotSearchItemVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Hot search keywords response
 */
@Data
public class HotSearchResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<HotSearchItemVo> items;
}
