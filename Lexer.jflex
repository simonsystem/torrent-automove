import java.util.*;
import com.google.common.collect.*;

%%

%class Lexer
%type ListMultimap<String, String>
%{
  private ListMultimap<String, String> result = ArrayListMultimap.create();
  private void put(String key) {
    put(key, OTHER);
  }
  private void put(String key, int state) {
    yybegin(state);
    result.put(key, yytext());
  }
%}
%state FOUND
%state EPISODENAME
%state OTHER

%%

[1,2]\d\d\d                             { put("year"); }
\d{3,4}p                                { put("resolution"); }
CAM|TS[^C]|TELESYNC|(DVD|BD)SCR|SCR     |
DDC|R5[\.\s]LINE|R5                     |
(WEB)?(DVD|HD|BR|BD|WEB|BluRay|HDTV)Rip |
DVDR|(HD|PD)TV|WEB-DL|WEBDL|BluRay      { put("type"); }
NTSC|PAL|xvid|divx|avi|mkv|[xh]\.?264   { put("video"); }
AAC2[\.\s]0|AAC|AC3|DTS|DD5\.?1         { put("audio"); }
MULTiSUBS|MULTi|NORDiC|DANiSH|SWEDiSH   |
NORWEGiAN|GERMAN|iTALiAN|FRENCH|SPANiSH { put("language"); }
[SD]UBBED                               { put("langtype"); }
UNRATED|DC|EXTENDED|3D|2D|\bNF\b        |
(Directors|EXTENDED)[\.\s](CUT|EDITION) { put("edition"); }
COMPLETE|LiMiTED|iNTERNAL               { put("tags"); }
REAL[\.\s]PROPER|PROPER|REPACK|READNFO  |
READ[\.\s]NFO|DiRFiX|NFOFiX             { put("release"); }
[A-Za-z0-9]+$                           { put("group"); }
S\d\d(-?S\d\d)?                         |
(S\d\d)?E\d\d\d?(-?E\d\d\d?)?           { put("episode", EPISODENAME); }


<YYINITIAL, FOUND>  [A-Za-z0-9]+        { put("name", FOUND); }
<EPISODENAME>       [A-Za-z0-9]+        { put("episodename", EPISODENAME); }
<OTHER>             [A-Za-z0-9]+        { put("other", OTHER); }

\.|\-                                   {}
<YYINITIAL>         \r|\n|\r\n          {}
<FOUND, EPISODENAME, OTHER> {
  <<EOF>>                               { yybegin(YYINITIAL); return result; }
  \r|\n|\r\n                            { yybegin(YYINITIAL); return result; }
}
[^]                   { throw new Error("Illegal character <"+yytext()+">"); }