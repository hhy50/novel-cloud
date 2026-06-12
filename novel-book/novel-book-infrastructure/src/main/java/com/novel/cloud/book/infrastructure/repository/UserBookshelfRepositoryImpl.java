package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.book.domain.entity.UserBookshelf;
import com.novel.cloud.book.domain.repository.UserBookshelfRepository;
import com.novel.cloud.book.infrastructure.dataobject.UserBookshelfDO;
import com.novel.cloud.book.infrastructure.mapper.UserBookshelfMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserBookshelfRepositoryImpl implements UserBookshelfRepository {

    private final UserBookshelfMapper userBookshelfMapper;

    @Override
    public List<UserBookshelf> findByUserId(Long userId) {
        List<UserBookshelfDO> bookshelfDOList = userBookshelfMapper.selectList(
                new LambdaQueryWrapper<UserBookshelfDO>()
                        .eq(UserBookshelfDO::getUserId, userId)
                        .orderByDesc(UserBookshelfDO::getLastReadTime)
        );

        return bookshelfDOList.stream()
                .map(bookshelfDO -> {
                    UserBookshelf bookshelf = new UserBookshelf();
                    BeanUtils.copyProperties(bookshelfDO, bookshelf);
                    return bookshelf;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserBookshelf findByUserIdAndBookId(Long userId, Long bookId) {
        UserBookshelfDO bookshelfDO = userBookshelfMapper.selectOne(
                new LambdaQueryWrapper<UserBookshelfDO>()
                        .eq(UserBookshelfDO::getUserId, userId)
                        .eq(UserBookshelfDO::getBookId, bookId)
                        .last("limit 1")
        );
        if (bookshelfDO == null) {
            return null;
        }
        UserBookshelf bookshelf = new UserBookshelf();
        BeanUtils.copyProperties(bookshelfDO, bookshelf);
        return bookshelf;
    }

    @Override
    public void save(UserBookshelf bookshelf) {
        UserBookshelfDO bookshelfDO = new UserBookshelfDO();
        BeanUtils.copyProperties(bookshelf, bookshelfDO);
        userBookshelfMapper.insert(bookshelfDO);
    }

    @Override
    public void deleteByUserIdAndBookId(Long userId, Long bookId) {
        userBookshelfMapper.delete(
                new LambdaQueryWrapper<UserBookshelfDO>()
                        .eq(UserBookshelfDO::getUserId, userId)
                        .eq(UserBookshelfDO::getBookId, bookId)
        );
    }

    @Override
    public void updateLastRead(Long userId, Long bookId, Long chapterId) {
        userBookshelfMapper.updateLastRead(userId, bookId, chapterId);
    }

    @Override
    public UserBookshelf addOrUpdate(Long userId, Long bookId) {
        UserBookshelfDO bookshelfDO = userBookshelfMapper.upsert(userId, bookId);

        UserBookshelf bookshelf = new UserBookshelf();
        BeanUtils.copyProperties(bookshelfDO, bookshelf);
        return bookshelf;
    }
}
