package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import ru.fmtk.khlystov.otus_java.currencies.EU;
import ru.fmtk.khlystov.otus_java.currencies.RU;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmImplTest {

    @Test
    void add_one() {
        Atm<EU> atm = new AtmImpl<>(EU.class);
        atm.add(EU.v10, 2);
        assertEquals(Map.of(EU.v10, 2), atm.audit());
    }

    @Test
    void add_severalIncreased() {
        Atm<RU> atm = getRuAtm();
        assertEquals(Map.of(
                        RU.v1, 5,
                        RU.v10, 4,
                        RU.v50, 3,
                        RU.v100, 2,
                        RU.v1000, 1),
                atm.audit());
    }

    @Test
    void getSum_isOk() {
        Atm<RU> atm = getRuAtm();
        Map<RU, Integer> cash = atm.getSum(BigDecimal.valueOf(53));
        assertEquals(Map.of(
                        RU.v1, 3,
                        RU.v50, 1),
                cash);
    }

    @Test
    void getSum_exactSum_isOk() {
        Atm<RU> atm = getRuAtm();
        Map<RU, Integer> cash = atm.getSum(BigDecimal.valueOf(134));
        assertEquals(Map.of(
                        RU.v1, 4,
                        RU.v10, 3,
                        RU.v100, 1),
                cash);
    }

    @Test
    void getSum_tooBigSum_isBad() {
        Atm<RU> atm = getRuAtm();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> atm.getSum(BigDecimal.valueOf(3000)));
        assertEquals("We cannot cashing sum 3000", exception.getMessage());
    }

    @Test
    void getSum_notFactorisedSum_isBad() {
        Atm<RU> atm = getRuAtm();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> atm.getSum(BigDecimal.valueOf(17)));
        assertEquals("We cannot cashing sum 17", exception.getMessage());
    }

    @Test
    void calcTotalSum_forEmptyAtm_isZero() {
        Atm<RU> atm = new AtmImpl<>(RU.class);
        assertEquals(BigDecimal.ZERO, atm.calcTotalSum());
    }

    @Test
    void calcTotalSum_for10By3Atm_is30() {
        Atm<RU> atm = new AtmImpl<>(RU.class);
        atm.add(RU.v10, 3);
        assertEquals(BigDecimal.valueOf(30), atm.calcTotalSum());
    }

    @Test
    void calcTotalSum_for10By3And1By2Atm_is32() {
        Atm<RU> atm = new AtmImpl<>(RU.class);
        atm.add(RU.v10, 3);
        atm.add(RU.v1, 2);
        assertEquals(BigDecimal.valueOf(32), atm.calcTotalSum());
    }

    @Test
    void calcTotalSum_forFractional_isExactValue() {
        Atm<RU> atm = getRuAtm();
        atm.add(RU.v0001, 1);
        assertEquals(BigDecimal.valueOf(1395.0001), atm.calcTotalSum());
    }

    private Atm<RU> getRuAtm() {
        Atm<RU> atm = new AtmImpl<>(RU.class);
        atm.add(RU.v1, 5);
        atm.add(RU.v10, 4);
        atm.add(RU.v50, 3);
        atm.add(RU.v100, 2);
        atm.add(RU.v1000, 1);
        return atm;
    }
}