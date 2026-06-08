package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.ReadingHistoryItemVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Reading history response
 */
@Data
public class ReadingHistoryResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<ReadingHistoryItemVo> records;
    private Integer total;
}
