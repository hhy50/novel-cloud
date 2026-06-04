package com.novel.cloud.book.app;

import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍应用服务
 * 职责：用例编排、事务控制，不写核心业务规则
 */
@Service
@RequiredArgsConstructor
public class BookAppService {

    private final BookInfoRepository bookInfoRepository;

    public Mono<BookDetailVo> getBookDetail(BookDetailQueryDto params) {
        BookInfo bookInfo = bookInfoRepository.findById(params.getBookId());
        BookDetailVo detailVo = new BookDetailVo();
        if (bookInfo != null) {
            detailVo.setBookId(bookInfo.getId());
            detailVo.setBookName(bookInfo.getBookName());
            detailVo.setAuthorName(bookInfo.getAuthorName());
            detailVo.setCategoryName(bookInfo.getCategoryName());
            detailVo.setCoverUrl(bookInfo.getCoverUrl());
            detailVo.setDescription(bookInfo.getDescription());
            detailVo.setFinished(bookInfo.getFinishedStatus() != null && bookInfo.getFinishedStatus() == 1);
        } else {
            detailVo.setBookId(params.getBookId());
            detailVo.setBookName(resolveBookTitle(params.getBookId()));
            detailVo.setAuthorName(resolveBookAuthor(params.getBookId()));
            detailVo.setCategoryName("玄幻");
            detailVo.setCoverUrl("https://static.example.com/book/cover/default.png");
            detailVo.setDescription("示例书籍详情，后续可替换为数据库真实数据。阅读器使用章节目录接口与章节内容接口按需加载正文。");
            detailVo.setFinished(Boolean.FALSE);
        }
        return Mono.just(detailVo);
    }

    public Mono<BookChapterListVo> getBookChapterList(BookChapterListQueryDto params) {
        BookChapterListVo chapterListVo = new BookChapterListVo();
        chapterListVo.setBookId(params.getBookId());
        chapterListVo.setChapters(buildChapters(resolveBookTitle(params.getBookId()))
                .stream()
                .map(this::toChapterSummary)
                .toList());
        return Mono.just(chapterListVo);
    }

    public Mono<BookChapterVo> getBookChapterContent(BookChapterContentQueryDto params) {
        List<BookChapterVo> chapters = buildChapters(resolveBookTitle(params.getBookId()));
        return Mono.just(
                chapters.stream()
                        .filter(chapter -> chapter.getChapterId().equals(params.getChapterId()))
                        .findFirst()
                        .orElseGet(() -> buildChapter(
                                params.getChapterId(),
                                "第 " + params.getChapterId() + " 章",
                                resolveBookTitle(params.getBookId()) + " 的补充章节内容，用于模拟单章按需加载。"
                        ))
        );
    }

    public Mono<BookstoreVo> getBookstore(BookstoreQueryDto params) {
        BookstoreVo bookstoreVo = new BookstoreVo();
        bookstoreVo.setHeaderTabs(List.of("HOT", "BEST"));
        bookstoreVo.setSections(List.of(
                buildSection(1, "编辑推荐", "适合 bookstore 样式 1 的横幅与横滑布局", List.of("hot", "editor"), List.of(
                        buildBook(101L, "月海来信", "林见川", "雾港、旧书店与未拆开的来信交织成一段缓慢展开的都市悬疑旅程。", "#5B4B8A", buildChapters("月海来信")),
                        buildBook(102L, "风穿过群山", "周予安", "山谷、雪线与失踪护林员的线索，让回乡故事多了层寻找真相的张力。", "#2F6E62", buildChapters("风穿过群山")),
                        buildBook(103L, "长街微光", "沈知意", "深夜便利店里汇聚起城市边缘人的命运切面。", "#D97A54", buildChapters("长街微光")),
                        buildBook(104L, "落雪之前", "顾长川", "一场冬天来临前的重逢，把旧友与旧案重新牵到一起。", "#4C7DA6", buildChapters("落雪之前")),
                        buildBook(105L, "云端档案", "季明舟", "科技公司内网泄露出一份改变人生的名单。", "#8A4F7D", buildChapters("云端档案")),
                        buildBook(106L, "潮汐尽头", "迟南乔", "海岸小镇上的失踪事件与天文潮汐产生了神秘呼应。", "#3B7F93", buildChapters("潮汐尽头"))
                )),
                buildSection(2, "本周热读", "适合 bookstore 样式 2 的列表样式", List.of("best", "weekly"), List.of(
                        buildBook(201L, "深蓝备忘录", "叶回", "前调查记者收到一份来自过去的匿名录音。", "#4169A8", buildChapters("深蓝备忘录")),
                        buildBook(202L, "鹿鸣山站", "安屿", "停运车站重新开放后，所有乘客都在等待一班不存在的列车。", "#5C8D6D", buildChapters("鹿鸣山站")),
                        buildBook(203L, "折页之间", "秦未", "修复师在古籍夹层里找到一段失落身世。", "#9B6B4E", buildChapters("折页之间")),
                        buildBook(204L, "第七个清晨", "宋遥", "连续重复的清晨里，主角试图找到唯一能改变结果的细节。", "#7A5F9A", buildChapters("第七个清晨")),
                        buildBook(205L, "风暴归航", "闻舟", "一艘远洋船在返航途中接收到不属于这个时代的求救信号。", "#2E6F95", buildChapters("风暴归航"))
                )),
                buildSection(4, "高分新书", "适合 bookstore 样式 4 的首图 + 横滑布局", List.of("new", "score"), List.of(
                        buildBook(401L, "白夜行", "东野圭吾", "跨越十九年的暗夜同行，真相藏在光与影的交界。", "#3A3D5C", buildChapters("白夜行")),
                        buildBook(402L, "三体", "刘慈欣", "宇宙级别的文明博弈，从红岸基地到黑暗森林。", "#1A3A4A", buildChapters("三体")),
                        buildBook(403L, "活着", "余华", "一个人一生的苦难与坚韧，在时代洪流中沉默前行。", "#6B4226", buildChapters("活着")),
                        buildBook(404L, "百年孤独", "马尔克斯", "马孔多小镇七代人的魔幻现实传奇。", "#4A6741", buildChapters("百年孤独")),
                        buildBook(405L, "挪威的森林", "村上春树", "青春、爱情与失去，在都市与森林之间徘徊。", "#2D5F4A", buildChapters("挪威的森林"))
                )),
                buildSection(8, "畅销排行", "适合 bookstore 样式 8 的双列网格布局", List.of("rank", "sell"), List.of(
                        buildBook(801L, "围城", "钱钟书", "城里的人想出去，城外的人想进来。", "#8B7355", buildChapters("围城")),
                        buildBook(802L, "平凡的世界", "路遥", "黄土高原上两兄弟的奋斗与命运。", "#7D6B5D", buildChapters("平凡的世界")),
                        buildBook(803L, "追风筝的人", "卡勒德·胡赛尼", "为你，千千万万遍。", "#B8860B", buildChapters("追风筝的人")),
                        buildBook(804L, "小王子", "圣埃克苏佩里", "只有用心灵才能看清事物的本质。", "#4682B4", buildChapters("小王子"))
                )),
                buildSection(13, "限时免费", "适合 bookstore 样式 13 的倒计时 + 横滑布局", List.of("free", "limit"), List.of(
                        buildBook(1301L, "人间失格", "太宰治", "生而为人，我很抱歉。", "#4A4A4A", buildChapters("人间失格")),
                        buildBook(1302L, "月亮与六便士", "毛姆", "满地都是六便士，他却抬头看见了月亮。", "#C0A96C", buildChapters("月亮与六便士")),
                        buildBook(1303L, "霍乱时期的爱情", "马尔克斯", "跨越半个世纪的爱情等待。", "#8B4513", buildChapters("霍乱时期的爱情")),
                        buildBook(1304L, "局外人", "加缪", "今天，妈妈死了。也许是昨天。", "#696969", buildChapters("局外人"))
                )),
                buildSection(16, "新书速递", "适合 bookstore 样式 16 的大图 + 列表布局", List.of("new", "speed"), List.of(
                        buildBook(1601L, "解忧杂货店", "东野圭吾", "一家连接过去与未来的杂货店。", "#DAA520", buildChapters("解忧杂货店")),
                        buildBook(1602L, "嫌疑人X的献身", "东野圭吾", "最纯粹的爱情与最精密的诡计。", "#2F4F4F", buildChapters("嫌疑人X的献身"))
                )),
                buildSection(24, "编辑私藏", "适合 bookstore 样式 24 的横向滚动小卡片", List.of("editor", "pick"), List.of(
                        buildBook(2401L, "了不起的盖茨比", "菲茨杰拉德", "我们继续奋力向前，逆水行舟。", "#D4AF37", buildChapters("了不起的盖茨比")),
                        buildBook(2402L, "1984", "乔治·奥威尔", "老大哥在看着你。", "#333333", buildChapters("1984")),
                        buildBook(2403L, "动物农场", "乔治·奥威尔", "所有动物生来平等，但有些动物比其他动物更平等。", "#556B2F", buildChapters("动物农场")),
                        buildBook(2404L, "美丽新世界", "赫胥黎", "人们感到痛苦的不是他们用笑声取代了思考，而是他们不知道自己为什么笑以及为什么不再思考。", "#708090", buildChapters("美丽新世界"))
                )),
                buildSection(25, "完本精选", "适合 bookstore 样式 25 的竖向列表布局", List.of("finish", "select"), List.of(
                        buildBook(2501L, "红楼梦", "曹雪芹", "满纸荒唐言，一把辛酸泪。", "#8B0000", buildChapters("红楼梦")),
                        buildBook(2502L, "三国演义", "罗贯中", "天下大势，分久必合，合久必分。", "#8B4513", buildChapters("三国演义")),
                        buildBook(2503L, "水浒传", "施耐庵", "八方共域，异姓一家。", "#556B2F", buildChapters("水浒传")),
                        buildBook(2504L, "西游记", "吴承恩", "敢问路在何方，路在脚下。", "#B8860B", buildChapters("西游记"))
                ))
        ));
        return Mono.just(bookstoreVo);
    }

    // ---- 以下为 mock 数据辅助方法 ----

    private String resolveBookTitle(Long bookId) {
        return "示例书籍-" + bookId;
    }

    private String resolveBookAuthor(Long bookId) {
        return "作者-" + bookId;
    }

    private List<BookChapterVo> buildChapters(String bookTitle) {
        List<BookChapterVo> chapters = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            chapters.add(buildChapter((long) i, "第 " + i + " 章", bookTitle + " 第" + i + "章的内容，这里是正文区域。"));
        }
        return chapters;
    }

    private BookChapterVo buildChapter(Long chapterId, String chapterTitle, String content) {
        BookChapterVo vo = new BookChapterVo();
        vo.setChapterId(chapterId);
        vo.setChapterTitle(chapterTitle);
        vo.setParagraphs(List.of(content));
        return vo;
    }

    private BookChapterVo toChapterSummary(BookChapterVo chapter) {
        BookChapterVo summary = new BookChapterVo();
        summary.setChapterId(chapter.getChapterId());
        summary.setChapterTitle(chapter.getChapterTitle());
        return summary;
    }

    private BookstoreSectionVo buildSection(int style, String title, String subtitle,
                                             List<String> tags, List<BookstoreBookVo> books) {
        BookstoreSectionVo section = new BookstoreSectionVo();
        section.setStyle(style);
        section.setTitle(title);
        section.setSubtitle(subtitle);
        section.setTags(tags);
        section.setBooks(books);
        return section;
    }

    private BookstoreBookVo buildBook(Long bookId, String title, String author,
                                       String description, String coverHexColor, List<BookChapterVo> chapters) {
        BookstoreBookVo book = new BookstoreBookVo();
        book.setBookId(bookId);
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setCoverHexColor(coverHexColor);
        book.setChapters(chapters);
        return book;
    }
}
