package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookPermission;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookRole;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookUser;
import com.github.axiangcoding.axbot.remote.kook.service.entity.resp.GuildRoleListData;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AxbotInteractiveFunc(command = InteractiveCommand.GUILD_MANAGE)
@Component
@Slf4j
public class FuncManageGuild extends AbstractInteractiveFunction {
    @Resource
    KookClient kookClient;

    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    BotConfProps botConfProps;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String userId = input.getUserId();
        String guildId = input.getGuildId();
        String channelId = input.getChannelId();
        KookResponse<KookUser> userView = kookClient.getUserView(userId, guildId);
        List<Long> userRoles = userView.getData().getRoles();
        String nickname = userView.getData().getNickname();
        try {
            KookResponse<GuildRoleListData> guildRoleList = kookClient.getGuildRoleList(guildId, null, null);
            List<KookRole> roles = guildRoleList.getData().getItems();

            boolean isAdmin = false;
            // 判断该用户的角色是否具备管理员权限
            for (KookRole role : roles) {
                if (userRoles.contains(role.getRoleId())) {
                    if (KookPermission.hasPermission(role.getPermissions(), KookPermission.ADMIN)) {
                        isAdmin = true;
                        break;
                    }
                }
            }
            if (!isAdmin) {
                log.info("user [{}] has no permission to manage guild [{}]", userId, guildId);
                return input.response(kookNoPermission(nickname));
            }
        } catch (RuntimeException e) {
            return input.response(kookBotHaveNoPermission(nickname));
        }
        String cmdPrefix = botConfProps.getTriggerMessagePrefix().get(0);
        String[] paramList = input.getParamList();
        if (paramList.length == 0) {
            return input.response(kookGetHelp(nickname, cmdPrefix));
        }

        String manageCmd = paramList[0];
        HashMap<String, Object> items = new HashMap<>();
        switch (manageCmd) {
            case "帮助" -> {
                return input.response(kookGetHelp(nickname, cmdPrefix));
            }
            case "B站直播通知" -> {
                if (paramList.length == 2 && "关".equals(paramList[1])) {
                    kookGuildSettingService.disableBiliRoomRemind(guildId);
                    return input.response(kookConfigSuccess(nickname, items));
                } else if ("开".equals(paramList[1]) && paramList.length == 3) {
                    if (!StringUtils.isNumeric(paramList[2])) {
                        return input.response(kookConfigError(nickname));
                    }
                    kookGuildSettingService.enableBiliRoomRemind(guildId, channelId, paramList[2]);
                    return input.response(kookConfigSuccess(nickname, items));
                } else {
                    return input.response(kookConfigError(nickname));
                }
            }
            case "战雷新闻播报" -> {
                if (paramList.length != 2) {
                    return input.response(kookConfigError(nickname));
                }
                if ("关".equals(paramList[1])) {
                    kookGuildSettingService.disableWtNewsRemind(guildId);
                    return input.response(kookConfigSuccess(nickname, items));
                } else if ("开".equals(paramList[1])) {
                    kookGuildSettingService.enableWtNewsRemind(guildId, channelId);
                    return input.response(kookConfigSuccess(nickname, items));
                } else {
                    return input.response(kookConfigError(nickname));
                }
            }
            case "战雷战绩查询" -> {
                // TODO
            }
            default -> {
                return input.response(kookConfigError(nickname));
            }
        }
        return input.response(kookConfigError(nickname));
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        return input.response(cqhttpGetHelp());
    }

    private String kookNoPermission(String nickname) {
        KookQuickCard card = new KookQuickCard("你好，%s".formatted(nickname), "danger");
        card.addModuleMdSection("您的账号未具备管理员权限，因此您无法管理AXBot！");
        card.addModuleMdSection("AXBot仅接受本服务器中具备管理员权限角色的用户的管理指令");
        card.addModuleDivider();
        card.addModuleContentSection("如果您是服务器所有者，请检查是否给自己分配了具备管理员权限的角色");
        return card.displayWithFooter();
    }

    private String kookBotHaveNoPermission(String nickname) {
        KookQuickCard card = new KookQuickCard("你好，%s".formatted(nickname), "danger");
        card.addModule(KookCardMessage.newHeader("配置失败！"));
        card.addModuleMdSection("AXBot可能未具备具有`管理角色权限`权限的角色身份，请赋予后重试");
        card.addModuleMdSection("如果问题一直存在请通知开发者");
        return card.displayWithFooter();
    }

    private String kookGetHelp(String nickname, String cmdPrefix) {
        KookQuickCard card = new KookQuickCard("你好，%s".formatted(nickname), "info");
        card.addModule(KookCardMessage.newHeader("可用的管理命令"));
        card.addModuleContentSection("请严格按照格式输入命令，否则命令不会被识别");

        commandDescription(cmdPrefix).forEach((k, v) -> {
            card.addModule(KookCardMessage.newSection(KookCardMessage.newKMarkdown(
                    KookMDMessage.code(k) + " - " + v)));
        });

        return card.displayWithFooter();
    }

    private String cqhttpGetHelp() {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("暂不支持配置");
        msg.addLine("当前暂不支持配置，如有需要请联系开发者");
        msg.addLine("该功能后续需要授权才能使用");
        msg.addLine("相关规则后续会公布，敬请期待");
        return msg.display();
    }

    private String kookConfigSuccess(String nickname, Map<String, Object> items) {
        KookQuickCard card = new KookQuickCard("你好，%s".formatted(nickname), "success");
        card.addModule(KookCardMessage.newHeader("配置修改成功"));

        items.forEach((k, v) -> {
            card.addModule(KookCardMessage.newSection(KookCardMessage.newKMarkdown(
                    KookMDMessage.bold(k) + ": " + v)));
        });

        return card.displayWithFooter();
    }

    private String kookConfigError(String nickname) {
        KookQuickCard card = new KookQuickCard("你好，%s".formatted(nickname), "danger");
        card.addModule(KookCardMessage.newHeader("配置修改失败"));

        card.addModuleMdSection("请检查配置字段是否正确！");
        card.addModuleMdSection("如果你不知道要怎么开始，请查看帮助");

        return card.displayWithFooter();
    }

    private Map<String, String> commandDescription(String prefix) {
        Map<String, String> cmds = new LinkedHashMap<>();
        cmds.put("%s 管理 B站直播通知 <开|关> [直播间号]".formatted(prefix), "配置B站直播通知的开关。设置为开时，则在当前频道通知");
        cmds.put("%s 管理 战雷新闻播报 <开|关>".formatted(prefix), "配置战雷新闻播报开关。设置为开时，则在当前频道播报");
        cmds.put("%s 管理 战雷战绩查询 <开|关>".formatted(prefix), "配置战雷战绩查询开关。设置为开时，则在当前频道允许查询");

        return cmds;
    }
}
