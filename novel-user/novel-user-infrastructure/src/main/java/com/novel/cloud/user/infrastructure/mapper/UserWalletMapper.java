package com.novel.cloud.user.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.user.infrastructure.dataobject.UserWalletDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface UserWalletMapper extends BaseMapper<UserWalletDO> {

    @Update("UPDATE t_user_wallet SET coins = #{coins}, total_coins = #{totalCoins} WHERE user_id = #{userId}")
    void updateCoins(@Param("userId") Long userId, @Param("coins") Long coins, @Param("totalCoins") Long totalCoins);

    @Update("UPDATE t_user_wallet SET points = #{points}, total_points = #{totalPoints} WHERE user_id = #{userId}")
    void updatePoints(@Param("userId") Long userId, @Param("points") Long points, @Param("totalPoints") Long totalPoints);
}
