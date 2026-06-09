package com.ruoyi.common.core.session;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.enums.OnlineStatus;

/**
 * 在线用户会话属性（Sa-Token版本）
 * 
 * @author ruoyi
 */
public class OnlineSession implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 会话ID */
    private String id;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String loginName;

    /** 部门名称 */
    private String deptName;
	
	/** 用户头像 */
	private String avatar;

    /** 登录IP地址 */
    private String host;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 在线状态 */
    private OnlineStatus status = OnlineStatus.on_line;

    /** 开始时间 */
    private Date startTimestamp;

    /** 最后访问时间 */
    private Date lastAccessTime;

    /** 超时时间 */
    private long timeout;

    /** 属性是否改变 优化session数据同步 */
    private transient boolean attributeChanged = false;

    /** 会话属性 */
    private Map<Object, Object> attributes;

    public OnlineSession()
    {
        this.startTimestamp = new Date();
        this.lastAccessTime = new Date();
        this.attributes = new HashMap<>();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public OnlineStatus getStatus()
    {
        return status;
    }

    public void setStatus(OnlineStatus status)
    {
        this.status = status;
    }

    public Date getStartTimestamp()
    {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp)
    {
        this.startTimestamp = startTimestamp;
    }

    public Date getLastAccessTime()
    {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime)
    {
        this.lastAccessTime = lastAccessTime;
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    public void markAttributeChanged()
    {
        this.attributeChanged = true;
    }

    public void resetAttributeChanged()
    {
        this.attributeChanged = false;
    }

    public boolean isAttributeChanged()
    {
        return attributeChanged;
    }

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
    public void setAttribute(Object key, Object value)
    {
        if (attributes == null)
        {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
        markAttributeChanged();
    }

    public Object getAttribute(Object key)
    {
        if (attributes == null)
        {
            return null;
        }
        return attributes.get(key);
    }

    public Object removeAttribute(Object key)
    {
        if (attributes == null)
        {
            return null;
        }
        Object removed = attributes.remove(key);
        if (removed != null)
        {
            markAttributeChanged();
        }
        return removed;
    }

    public Map<Object, Object> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(Map<Object, Object> attributes)
    {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("loginName", getLoginName())
            .append("deptName", getDeptName())
            .append("avatar", getAvatar())
            .append("host", getHost())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("startTimestamp", getStartTimestamp())
            .append("lastAccessTime", getLastAccessTime())
            .append("timeout", getTimeout())
            .append("attributeChanged", isAttributeChanged())
            .toString();
    }
}
