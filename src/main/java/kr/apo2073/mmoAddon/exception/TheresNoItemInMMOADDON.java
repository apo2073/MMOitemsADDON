package kr.apo2073.mmoAddon.exception;

public class TheresNoItemInMMOADDON extends NullPointerException {
    public TheresNoItemInMMOADDON() {
        super("아이템 설정이 안되어 있습니다! 플러그인 개발자에게 문의해주세요, ");
    }
}
