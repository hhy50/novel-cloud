package com.novel.payment.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.payment.domain.entity.PaymentOrder;
import com.novel.payment.domain.repository.PaymentOrderRepository;
import com.novel.payment.infrastructure.dataobject.PaymentOrderDO;
import com.novel.payment.infrastructure.mapper.PaymentOrderMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentOrderRepositoryImpl implements PaymentOrderRepository {

    private final PaymentOrderMapper paymentOrderMapper;

    @Override
    public void save(PaymentOrder paymentOrder) {
        PaymentOrderDO paymentOrderDO = new PaymentOrderDO();
        BeanUtils.copyProperties(paymentOrder, paymentOrderDO);
        paymentOrderMapper.insert(paymentOrderDO);
        // 回填自增ID
        paymentOrder.setId(paymentOrderDO.getId());
    }

    @Override
    public void updateById(PaymentOrder paymentOrder) {
        PaymentOrderDO paymentOrderDO = new PaymentOrderDO();
        BeanUtils.copyProperties(paymentOrder, paymentOrderDO);
        paymentOrderMapper.updateById(paymentOrderDO);
    }

    @Override
    public PaymentOrder findByOrderNo(String orderNo) {
        PaymentOrderDO paymentOrderDO = paymentOrderMapper.selectOne(
                new LambdaQueryWrapper<PaymentOrderDO>()
                        .eq(PaymentOrderDO::getOrderNo, orderNo)
        );
        if (paymentOrderDO == null) {
            return null;
        }
        PaymentOrder paymentOrder = new PaymentOrder();
        BeanUtils.copyProperties(paymentOrderDO, paymentOrder);
        return paymentOrder;
    }

    @Override
    public List<PaymentOrder> findByUserId(Long userId) {
        List<PaymentOrderDO> doList = paymentOrderMapper.selectList(
                new LambdaQueryWrapper<PaymentOrderDO>()
                        .eq(PaymentOrderDO::getUserId, userId)
                        .orderByDesc(PaymentOrderDO::getCreateTime)
        );
        return doList.stream().map(d -> {
            PaymentOrder paymentOrder = new PaymentOrder();
            BeanUtils.copyProperties(d, paymentOrder);
            return paymentOrder;
        }).collect(Collectors.toList());
    }
}
