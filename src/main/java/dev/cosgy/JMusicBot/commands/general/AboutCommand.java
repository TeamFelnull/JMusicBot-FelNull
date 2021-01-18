/*
 * Copyright 2018-2020 Cosgy Dev
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package dev.cosgy.JMusicBot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * @author Cosgy Dev
 */
@CommandInfo(
        name = "About",
        description = "ボットに関する情報を表示します"
)
@Author("FelNull")
public class AboutCommand extends Command {
    private final Color color;
    private final String description;
    private final Permission[] perms;
    private final String[] features;
    private boolean IS_AUTHOR = true;
    private String REPLACEMENT_ICON = "+";
    private String oauthLink;

    public AboutCommand(Color color, String description, String[] features, Permission... perms) {
        this.color = color;
        this.description = description;
        this.features = features;
        this.name = "about";
        this.help = "ボットに関する情報を表示します";
        this.guildOnly = false;
        this.perms = perms;
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
    }

    public void setIsAuthor(boolean value) {
        this.IS_AUTHOR = value;
    }

    public void setReplacementCharacter(String value) {
        this.REPLACEMENT_ICON = value;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (oauthLink == null) {
            try {
                ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, perms) : "";
            } catch (Exception e) {
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("招待リンクを生成できませんでした ", e);
                oauthLink = "";
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild() == null ? color : event.getGuild().getSelfMember().getColor());
        builder.setAuthor("" + event.getSelfUser().getName() + "について!", null, event.getSelfUser().getAvatarUrl());
        String CosgyOwner = "FelNullが運営、開発をしています。";
        /*boolean join = !(event.getClient().getServerInvite() == null || event.getClient().getServerInvite().isEmpty());
        boolean inv = !oauthLink.isEmpty();
        String invline = "\n"+ (join ? "FelNull公式チャンネル [`こちら`](https://discord.gg/X9BUF9A)" : (inv ? "へお願いします。 " : ""));
                + (inv ? (join ? ", または " : "") + "あなたのサーバーに[`招待リンク`](" + oauthLink + ") " : "で招待することができます。") + "!"*/
        String author = event.getJDA().getUserById(event.getClient().getOwnerId()) == null ? "<@" + event.getClient().getOwnerId() + ">"
                : event.getJDA().getUserById(event.getClient().getOwnerId()).getName();
        StringBuilder descr = new StringBuilder().append("こんにちは！ **").append(event.getSelfUser().getName()).append("**です。 ")
                .append(description).append("は、").append(JDAUtilitiesInfo.AUTHOR + "の[コマンド拡張](" + JDAUtilitiesInfo.GITHUB + ") (")
                .append(JDAUtilitiesInfo.VERSION).append(")と[JDAライブラリ](https://github.com/DV8FromTheWorld/JDA) (")
                .append(JDAInfo.VERSION).append(")を使用しており、").append((IS_AUTHOR ? CosgyOwner : author + "が所有しています。"))
                .append(event.getSelfUser().getName()).append("についての質問などは[FelNull公式チャンネル](https://discord.gg/X9BUF9A)へお願いします。")
                .append("\nこのボットの使用方法は`").append(event.getClient().getTextualPrefix()).append(event.getClient().getHelpWord())
                .append("`で確認することができます。").append("\n\n機能の特徴： ```css");
        for (String feature : features)
            descr.append("\n").append(event.getClient().getSuccess().startsWith("<") ? REPLACEMENT_ICON : event.getClient().getSuccess()).append(" ").append(feature);
        descr.append(" ```");
        builder.setDescription(descr);
        event.getJDA().getShardInfo();
        builder.addField("ステータス", (event.getClient()).getTotalGuilds() + " サーバー\nシャード " + (event.getJDA().getShardInfo().getShardId() + 1)
                + "/" + event.getJDA().getShardInfo().getShardTotal(), true);
        builder.addField("", event.getJDA().getUsers().size() + " ユーザーのシャード\n" + event.getJDA().getGuilds().size() + " サーバー", true);
        builder.addField("", event.getJDA().getTextChannels().size() + " テキストチャンネル\n" + event.getJDA().getVoiceChannels().size() + " ボイスチャンネル", true);
        builder.setFooter("再起動が行われた時間：", "https://www.cosgy.tokyo/wp-content/uploads/2020/03/restart.jpg");
        builder.setTimestamp(event.getClient().getStartTime());
        event.reply(builder.build());
    }

}
