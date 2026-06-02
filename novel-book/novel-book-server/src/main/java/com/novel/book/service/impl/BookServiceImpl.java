package com.novel.book.service.impl;

import com.novel.book.dto.BookChapterVo;
import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.book.dto.BookstoreBookVo;
import com.novel.book.dto.BookstoreQueryDto;
import com.novel.book.dto.BookstoreSectionVo;
import com.novel.book.dto.BookstoreVo;
import com.novel.book.service.BookService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Override
    public Mono<BookDetailVo> getBookDetail(BookDetailQueryDto params) {
        BookDetailVo detailVo = new BookDetailVo();
        detailVo.setBookId(params.getBookId());
        detailVo.setBookName("诡秘之主");
        detailVo.setAuthorName("爱潜水的乌贼");
        detailVo.setCategoryName("玄幻");
        detailVo.setCoverUrl("https://static.example.com/book/cover/default.png");
        detailVo.setDescription("示例书籍详情，后续可替换为数据库真实数据。");
        detailVo.setFinished(Boolean.TRUE);
        return Mono.just(detailVo);
    }

    @Override
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
                        buildBook(301L, "白塔夜行", "姜回", "深夜广播节目把一宗旧案重新送回公众视野。", "#7654A3", buildChapters("白塔夜行")),
                        buildBook(302L, "海上电台", "陈既明", "废弃电台深夜自动播出的节目，只有少数人能听见。", "#2E7A88", buildChapters("海上电台")),
                        buildBook(303L, "未寄出的春天", "顾青禾", "一沓旧信串联起三代人的误解与和解。", "#C56B6B", buildChapters("未寄出的春天")),
                        buildBook(304L, "时间的灰烬", "黎舟", "考古现场挖出的怀表，让时间线开始错位。", "#7E6750", buildChapters("时间的灰烬")),
                        buildBook(305L, "群星诊所", "林暮", "专治梦境创伤的诊所里，每位病人都带着一段异样记忆。", "#5B7FB3", buildChapters("群星诊所"))
                )),
                buildSection(8, "榜单速览", "适合 bookstore 样式 8 的双列卡片布局", List.of("rank"), List.of(
                        buildBook(401L, "夜航南岸", "陆时予", "跨海大桥通车前夜，一名失踪工程师留下最后坐标。", "#4778A5", buildChapters("夜航南岸")),
                        buildBook(402L, "醒在第九层", "祝宁", "高层公寓里每层住户都在隐藏某种身份。", "#915B73", buildChapters("醒在第九层")),
                        buildBook(403L, "霜落时分", "谢昭", "一场山火之后，归乡律师在旧宅发现意外遗嘱。", "#5E7C54", buildChapters("霜落时分")),
                        buildBook(404L, "异乡邮差", "闻柯", "从不写信的人，却成了偏远小镇唯一的邮差。", "#BA7852", buildChapters("异乡邮差")),
                        buildBook(405L, "玻璃海沟", "黎森", "潜海项目的事故调查引出海底实验的隐秘章节。", "#3D6C9C", buildChapters("玻璃海沟")),
                        buildBook(406L, "站台尽头", "温澈", "最后一班地铁之后，仍有人在站台等待不存在的乘客。", "#6A5E96", buildChapters("站台尽头"))
                )),
                buildSection(13, "轻量追更", "适合 bookstore 样式 13 的横向书架布局", List.of("daily"), List.of(
                        buildBook(501L, "雨幕回廊", "苏问", "一场暴雨让整栋老楼住户被迫重新认识彼此。", "#5879A2", buildChapters("雨幕回廊")),
                        buildBook(502L, "边界旅馆", "姜稚", "旅馆只接待在现实边缘徘徊的人。", "#8C5A73", buildChapters("边界旅馆")),
                        buildBook(503L, "纸鹤与鲸", "唐屿", "海边小城的成长记忆，被一封跨年信件再次打开。", "#4A8C83", buildChapters("纸鹤与鲸")),
                        buildBook(504L, "回声仓库", "陆沉", "一间无人仓库保存着别人不愿面对的声音。", "#A16C55", buildChapters("回声仓库")),
                        buildBook(505L, "南方来客", "何一川", "陌生人的到来打破了平静社区多年的默契。", "#6B6E9B", buildChapters("南方来客")),
                        buildBook(506L, "灯塔编号 17", "沈时", "海上雾季来临前，灯塔看守人收到了一份调岗通知。", "#3C7897", buildChapters("灯塔编号 17"))
                )),
                buildSection(16, "热门排行", "适合 bookstore 样式 16 的榜首突出布局", List.of("rank", "hot"), List.of(
                        buildBook(601L, "回潮日记", "程岸", "潮汐表上多出的一页，记录着本不该发生的相遇。", "#4D7DA1", buildChapters("回潮日记")),
                        buildBook(602L, "北境备忘", "裴川", "边境观测站里留下的手记，正在改变后来者的选择。", "#66785A", buildChapters("北境备忘")),
                        buildBook(603L, "空白剧场", "沈妍", "一家停演多年的剧场，突然重启只演给少数人看。", "#9A5F7E", buildChapters("空白剧场")),
                        buildBook(604L, "长桥旧事", "俞清", "桥梁修复计划唤起整座城对一场旧事故的记忆。", "#B2734F", buildChapters("长桥旧事")),
                        buildBook(605L, "晨雾识别码", "裴星河", "程序员在定位异常里找到现实被篡改的证据。", "#3F79A8", buildChapters("晨雾识别码"))
                )),
                buildSection(24, "主编精选", "适合 bookstore 样式 24 的头图 + 横向列表", List.of("editor"), List.of(
                        buildBook(701L, "静水回声", "周宁", "看似平静的内陆湖，埋着一份迟来的调查真相。", "#4A74A0", buildChapters("静水回声")),
                        buildBook(702L, "最后的注脚", "林槿", "文学研究生在手稿尾注里发现作者失踪前的隐秘讯息。", "#8A617A", buildChapters("最后的注脚")),
                        buildBook(703L, "灰灯巷", "顾念", "一条常年昏暗的小巷，住着整座城市最会保守秘密的人。", "#A96D4D", buildChapters("灰灯巷")),
                        buildBook(704L, "沉默剧本", "贺屿", "新人编剧收到一部未署名剧本，情节竟预演现实。", "#6570A1", buildChapters("沉默剧本")),
                        buildBook(705L, "暮色信号", "叶舟", "山区基站恢复供电后，发出一串反复重复的坐标。", "#3E8090", buildChapters("暮色信号")),
                        buildBook(706L, "风停之前", "段衡", "山地救援队在停风前一小时接到一通奇怪来电。", "#6D8157", buildChapters("风停之前"))
                )),
                buildSection(25, "适合一口气看完", "适合 bookstore 样式 25 的左右分栏布局", List.of("finish"), List.of(
                        buildBook(801L, "夜色合页", "许临", "一本合上又自动翻开的旧日记，迫使主人公重查家族旧案。", "#5277A7", buildChapters("夜色合页")),
                        buildBook(802L, "荒原来电", "顾既白", "无人区基站忽然接收到多年以前的通话记录。", "#986072", buildChapters("荒原来电")),
                        buildBook(803L, "远山长夏", "迟夏", "一个关于返乡、守望与重新定义亲密关系的夏日故事。", "#5C8B66", buildChapters("远山长夏")),
                        buildBook(804L, "逆风邮局", "时洛", "风暴季来临前，海岛邮局要送出最后一批信件。", "#B5744E", buildChapters("逆风邮局")),
                        buildBook(805L, "落幕时差", "纪沉", "电影节闭幕后，一卷失踪胶片重新出现。", "#6B62A0", buildChapters("落幕时差"))
                ))
        ));
        return Mono.just(bookstoreVo);
    }

    private BookstoreSectionVo buildSection(Integer style,
                                            String title,
                                            String subtitle,
                                            List<String> tags,
                                            List<BookstoreBookVo> books) {
        BookstoreSectionVo sectionVo = new BookstoreSectionVo();
        sectionVo.setStyle(style);
        sectionVo.setTitle(title);
        sectionVo.setSubtitle(subtitle);
        sectionVo.setTags(tags);
        sectionVo.setShowViewAll(Boolean.TRUE);
        sectionVo.setBooks(books);
        return sectionVo;
    }

    private BookstoreBookVo buildBook(Long bookId,
                                      String title,
                                      String author,
                                      String description,
                                      String coverHexColor,
                                      List<BookChapterVo> chapters) {
        BookstoreBookVo bookVo = new BookstoreBookVo();
        bookVo.setBookId(bookId);
        bookVo.setTitle(title);
        bookVo.setAuthor(author);
        bookVo.setDescription(description);
        bookVo.setCoverHexColor(coverHexColor);
        bookVo.setChapters(chapters);
        return bookVo;
    }

    private List<BookChapterVo> buildChapters(String title) {
        return List.of(
                buildChapter(1L, "第一章", title + " 的第一章内容，用于前端进入阅读页时展示基础正文。"),
                buildChapter(2L, "第二章", title + " 的第二章内容，补充人物与冲突推进。"),
                buildChapter(3L, "第三章", title + " 的第三章内容，为阅读器提供连续的章节翻页体验。")
        );
    }

    private BookChapterVo buildChapter(Long chapterId, String chapterTitle, String baseText) {
        BookChapterVo chapterVo = new BookChapterVo();
        chapterVo.setChapterId(chapterId);
        chapterVo.setChapterTitle(chapterTitle);
        chapterVo.setParagraphs(List.of(
                baseText + " 第 1 段。",
                baseText + " 第 2 段。",
                baseText + " 第 3 段。"
        ));
        return chapterVo;
    }
}
