package sns.sn.systems.pharmacist.prescription.viewmodel

import android.arch.lifecycle.ViewModel
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.tech.Ndef
import android.util.Log
import android.view.View
import sns.sn.systems.classes.F
import sns.sn.systems.classes.M
import sns.sn.systems.encryption.Encrypt
import sns.sn.systems.pharmacist.consumption.view.ReadConsumptionActivity
import sns.sn.systems.pharmacist.prescription.model.ReadWritePrescriptionModel
import sns.sn.systems.pharmacist.prescription.view.ReadWritePrescriptionActivity
import java.io.IOException
import java.util.*





class ReadWritePrescViewModel (private val listner : ReadWritePrescCallbacks) : ViewModel()
{


    private val readWritePrescriptionModel : ReadWritePrescriptionModel
    private val MSG_DIRECTION_COMMAND: Byte = 0x0
    private val MSG_DIRECTION_RESPONSE: Byte = 0x1
   // var response: List<ByteArray>? = null


    init
    {
        this.readWritePrescriptionModel = ReadWritePrescriptionModel("")
        readWritePrescriptionModel.setPrescriptionString(ReadWritePrescriptionActivity.PRESC_STRING)
    }
    //create function to process read prescription button click
    fun onReadPrescriptionClick(v:View)
    {
        try
        {
            var decryptInput = Encrypt.getStringFromDevice(ReadWritePrescriptionActivity.ndef);


            if(decryptInput != null)
            {


                //decrypt string here
                var message = Encrypt.decryptString(decryptInput.substring(4))

                //patient details
                var PT_DOB = message.get(0) + "" + message.get(1) + "" + message.get(2) + "" + message.get(3) + "" + message.get(4) + "" + message.get(5) + "" + message.get(6) + "" + message.get(7);
                var PT_GENDER = message.get(8)
                var PT_ID = message.get(9) + "" + message.get(10) + "" + message.get(11) + "" + message.get(12) + "" + message.get(13) + "" + message.get(14) + "" + message.get(15) + "" + message.get(16)

                //prescription details
                var PRESC_NAME = message.get(17) + "" + message.get(18) + "" + message.get(19) + "" + message.get(20) + "" + message.get(21) + "" + message.get(22) + "" + message.get(23) + "" + message.get(24) + "" + message.get(25) + "" + message.get(26) + "" + message.get(27) + "" + message.get(28) + "" + message.get(29) + "" + message.get(30) + "" + message.get(31)
                var PRESC_NUMBER = message.get(32) + "" + message.get(33) + "" + message.get(34) + "" + message.get(35) + "" + message.get(36) + "" + message.get(37) + "" + message.get(38) + "" + message.get(39)
                var PRESC_STRENGTH = message.get(40) + "" + message.get(41) + "" + message.get(42) + "" + message.get(43)
                var PRESC_DRUG_FORM = message[44] + "" + message[45] + "" + message[46]
                var PRESC_FREQUENCY = message.get(47) + "" + message.get(48) + "" + message.get(49) + "" + message.get(50) + "" + message.get(51) + "" + message.get(52) + "" + message.get(53) + "" + message.get(54) + "" + message.get(55) + "" + message.get(56) + "" + message.get(57) + "" + message.get(58) + "" + message.get(59) + "" + message.get(60) + "" + message.get(61) + "" + message.get(62) + "" + message.get(63) + "" + message.get(64) + "" + message.get(65) + "" + message.get(66) + "" + message.get(67) + "" + message.get(68) + "" + message.get(69) + "" + message.get(70) + "" + message.get(71) + "" + message.get(72) + "" + message.get(73) + "" + message.get(74) + "" + message.get(75) + "" + message.get(76) + "" + message.get(77) + "" + message.get(78) + "" + message.get(79) + "" + message.get(80) + "" + message.get(81) + "" + message.get(82) + "" + message.get(83) + "" + message.get(84) + "" + message.get(85) + "" + message.get(86) + "" + message.get(87) + "" + message.get(88) + "" + message.get(89) + "" + message.get(90) + "" + message.get(91) + "" + message.get(92) + "" + message.get(93) + "" + message.get(94) + "" + message.get(95) + "" + message.get(96) + "" + message.get(97) + "" + message.get(98) + "" + message.get(99) + "" + message.get(100) + "" + message.get(101) + "" + message.get(102) + "" + message.get(103) + "" + message.get(104) + "" + message.get(105) + "" + message.get(106) + "" + message.get(107) + "" + message.get(108) + "" + message.get(109) + "" + message.get(110) + "" + message.get(111) + "" + message.get(112) + "" + message.get(113) + "" + message.get(114) + "" + message.get(115) + "" + message.get(116) + "" + message.get(117) + "" + message.get(118) + "" + message.get(119) + "" + message.get(120) + "" + message.get(121) + "" + message.get(122) + "" + message.get(123) + "" + message.get(124) + "" + message.get(125) + "" + message.get(126) + "" + message.get(127) + "" + message.get(128) + "" + message.get(129) + "" + message.get(130) + "" + message.get(131) + "" + message.get(132) + "" + message.get(133) + "" + message.get(134) + "" + message.get(135) + "" + message.get(136) + "" + message.get(137) + "" + message.get(138) + "" + message.get(139) + "" + message.get(140) + "" + message.get(141) + "" + message.get(142) + "" + message.get(143) + "" + message.get(144) + "" + message.get(145) + "" + message.get(146) + "" + message.get(147) + "" + message.get(148) + "" + message.get(149) + "" + message.get(150) + "" + message.get(151) + "" + message.get(152) + "" + message.get(153) + "" + message.get(154) + "" + message.get(155) + "" + message.get(156) + "" + message.get(157) + "" + message.get(158) + "" + message.get(159) + "" + message.get(160) + "" + message.get(161) + "" + message.get(162) + "" + message.get(163) + "" + message.get(164) + "" + message.get(165) + "" + message.get(166) + "" + message.get(167) + "" + message.get(168) + "" + message.get(169) + "" + message.get(170) + "" + message.get(171) + "" + message.get(172) + "" + message.get(173) + "" + message.get(174) + "" + message.get(175) + "" + message.get(176) + "" + message.get(177) + "" + message.get(178) + "" + message.get(179) + "" + message.get(180) + "" + message.get(181) + "" + message.get(182) + "" + message.get(183) + "" + message.get(184) + "" + message.get(185) + "" + message.get(186)
                var PRESC_EXP_DATE = message.get(187) + "" + message.get(188) + "" + message.get(189) + "" + message.get(190) + "" + message.get(191) + "" + message.get(192) + "" + message.get(193) + "" + message.get(194)
                var PRESC_PT_COST = message.get(195) + "" + message.get(196) + "" + message.get(197) + "" + message.get(198) + "" + message.get(199) + "" + message.get(200)
                var PRESC_MFG = message.get(201) + "" + message.get(202) + "" + message.get(203) + "" + message.get(204)
                var PRESC_LOT_NUMBER = message.get(205) + "" + message.get(206) + "" + message.get(207) + "" + message.get(208) + "" + message.get(209) + "" + message.get(210) + "" + message.get(211) + "" + message.get(212) + "" + message.get(213)
                var PRESC_QUANTITY = message.get(214) + "" + message.get(215) + "" + message.get(216)
                var PRESC_DAY_SPLY = message.get(217) + "" + message.get(218)
                var PRESC_REFILL_REMAIN = message.get(219) + "" + message.get(220)
                var PRESC_DAW_CODE = message.get(221)
                var PRESC_ISSUE_DATE = message.get(222) + "" + message.get(223) + "" + message.get(224) + "" + message.get(225) + "" + message.get(226) + "" + message.get(227) + "" + message.get(228) + "" + message.get(229)
                var PRESC_DATE_FILLED = message.get(230) + "" + message.get(231) + "" + message.get(232) + "" + message.get(233) + "" + message.get(234) + "" + message.get(235) + "" + message.get(236) + "" + message.get(237)

                //pharmacy details
                var PHARM_NAME = message.get(238) + "" + message.get(239) + "" + message.get(240) + "" + message.get(241) + "" + message.get(242) + "" + message.get(243) + "" + message.get(244) + "" + message.get(245) + "" + message.get(246) + "" + message.get(247) + "" + message.get(248) + "" + message.get(249) + "" + message.get(250) + "" + message.get(251) + "" + message.get(252) + "" + message.get(253) + "" + message.get(254) + "" + message.get(255) + "" + message.get(256) + "" + message.get(257) + "" + message.get(258) + "" + message.get(259) + "" + message.get(260) + "" + message.get(261) + "" + message.get(262)
                var PHARM_ADDRESS = message.get(263) + "" + message.get(264) + "" + message.get(265) + "" + message.get(266) + "" + message.get(267) + "" + message.get(268) + "" + message.get(269) + "" + message.get(270) + "" + message.get(271) + "" + message.get(272) + "" + message.get(273) + "" + message.get(274) + "" + message.get(275) + "" + message.get(276) + "" + message.get(277) + "" + message.get(278) + "" + message.get(279) + "" + message.get(280) + "" + message.get(281) + "" + message.get(282) + "" + message.get(283) + "" + message.get(284) + "" + message.get(285) + "" + message.get(286) + "" + message.get(287) + "" + message.get(288) + "" + message.get(289) + "" + message.get(290) + "" + message.get(291) + "" + message.get(292) + "" + message.get(293) + "" + message.get(294) + "" + message.get(295) + "" + message.get(296) + "" + message.get(297) + "" + message.get(298) + "" + message.get(299) + "" + message.get(300) + "" + message.get(301) + "" + message.get(302) + "" + message.get(303) + "" + message.get(304) + "" + message.get(305) + "" + message.get(306) + "" + message.get(307) + "" + message.get(308) + "" + message.get(309) + "" + message.get(310) + "" + message.get(311) + "" + message.get(312) + "" + message.get(313) + "" + message.get(314) + "" + message.get(315) + "" + message.get(316) + "" + message.get(317) + "" + message.get(318) + "" + message.get(319)
                var PHARM_NUMBER = message.get(320) + "" + message.get(321) + "" + message.get(322) + "" + message.get(323) + "" + message.get(324) + "" + message.get(325) + "" + message.get(326) + "" + message.get(327) + "" + message.get(328) + "" + message.get(329)

                //provide details
                var PROV_NUMBER = message.get(330) + "" + message.get(331) + "" + message.get(332) + "" + message.get(333) + "" + message.get(334) + "" + message.get(335) + "" + message.get(336) + "" + message.get(337) + "" + message.get(338) + "" + message.get(339)
                var PROV_FAX_NUMBER = message.get(340) + "" + message.get(341) + "" + message.get(342) + "" + message.get(343) + "" + message.get(344) + "" + message.get(345) + "" + message.get(346) + "" + message.get(347) + "" + message.get(348) + "" + message.get(349)
                var PROV_DOC_NAME = message.get(350) + "" + message.get(351) + "" + message.get(352) + "" + message.get(353) + "" + message.get(354) + "" + message.get(355) + "" + message.get(356) + "" + message.get(357) + "" + message.get(358) + "" + message.get(359) + "" + message.get(360) + "" + message.get(361) + "" + message.get(362) + "" + message.get(363) + "" + message.get(364) + "" + message.get(365) + "" + message.get(366) + "" + message.get(367) + "" + message.get(368) + "" + message.get(369) + "" + message.get(370) + "" + message.get(371) + "" + message.get(372) + "" + message.get(373) + "" + message.get(374)
                //var ENCRYPTION_KEY = message.get(375)+""+message.get(376)+""+message.get(377)+""+message.get(378)+""+message.get(379)+""+message.get(380)+""+message.get(381)+""+message.get(382)+""+message.get(383)+""+message.get(384)+""+message.get(385)+""+message.get(386)+""+message.get(387)+""+message.get(388)+""+message.get(389)+""+message.get(390)+""+message.get(391)+""+message.get(392)+""+message.get(393)+""+message.get(394)+""+message.get(395)+""+message.get(396)+""+message.get(397)+""+message.get(398)+""+message.get(399)+""+message.get(400)+""+message.get(401)+""+message.get(402)+""+message.get(403)+""+message.get(404)+""+message.get(405)+""+message.get(406)+""+message.get(407)+""+message.get(408)+""+message.get(409)+""+message.get(410)+""+message.get(411)+""+message.get(412)+""+message.get(413)+""+message.get(414)+""+message.get(415)+""+message.get(416)+""+message.get(417)+""+message.get(418)+""+message.get(419)+""+message.get(420)+""+message.get(421)+""+message.get(422)+""+message.get(423)+""+message.get(424)+""+message.get(425)+""+message.get(426)+""+message.get(427)+""+message.get(428)+""+message.get(429)+""+message.get(430)+""+message.get(431)+""+message.get(432)+""+message.get(433)+""+message.get(434)+""+message.get(435)+""+message.get(436)+""+message.get(437)+""+message.get(438)+""+message.get(439)+""+message.get(440)+""+message.get(441)+""+message.get(442)+""+message.get(443)+""+message.get(444)+""+message.get(445)+""+message.get(446)+""+message.get(447)+""+message.get(448)+""+message.get(449)+""+message.get(450)+""+message.get(451)+""+message.get(452)+""+message.get(453)+""+message.get(454)

                var PRESCRIPTION_DETAILS = PT_DOB + "#" + PT_GENDER + "#" + PT_ID + "#" + PRESC_NAME + "#" + PRESC_NUMBER + "#" + PRESC_STRENGTH + "#" + PRESC_DRUG_FORM + "#" + PRESC_FREQUENCY + "#" + PRESC_EXP_DATE + "#" + PRESC_PT_COST + "#" + PRESC_MFG + "#" + PRESC_LOT_NUMBER + "#" + PRESC_QUANTITY + "#" + PRESC_DAY_SPLY + "#" + PRESC_REFILL_REMAIN + "#" + PRESC_DAW_CODE + "#" + PRESC_ISSUE_DATE + "#" + PRESC_DATE_FILLED + "#" + PHARM_NAME + "#" + PHARM_ADDRESS + "#" + PHARM_NUMBER + "#" + PROV_NUMBER + "#" + PROV_FAX_NUMBER + "#" + PROV_DOC_NAME

                listner.readPrescriptionSuccess(PRESCRIPTION_DETAILS)
            }
        }
        catch (e :Exception)
        {

            e.printStackTrace()
        }
    }

    //create function to process write prescription button click
    fun onWritePrescriptionClick(v:View)
    {

         //code for start theropy
         val MSG_ID_START: Byte = 0x57
         val command = byteArrayOf(MSG_ID_START, MSG_DIRECTION_COMMAND, 0, 0, 0, 0, 10, 0, 0, 0)
         val epoch = System.currentTimeMillis() / 1000
         command[2] = (epoch and 0x000000FF).toByte()
         command[3] = (epoch shr 8 and 0x000000FF).toByte()
         command[4] = (epoch shr 16 and 0x000000FF).toByte()
         command[5] = (epoch shr 24 and 0x000000FF).toByte()

        var responses = writeReadNdef(command)
        var expected = byteArrayOf(MSG_ID_START, MSG_DIRECTION_RESPONSE, 0, 0, 0, 0)

        if (responses == null)
        {
            F.t("Tap device on correct position.")

        }
        else if (responses.isEmpty())
        {
           // Log.e("NDEF_RESPONSE", "responses : Error. No response received.")
            listner.notifyUser("No response received.")
        }
        else
        {
            if (Arrays.equals(responses[0], expected))
            {
               // Log.e("NDEF_RESPONSE", "responses : "+responses[0])
                listner.notifyUser("Therapy started")
                //code for write prescription
                //encrypt patient details here
                var PRESCRIPTION_ARRAY = Encrypt.encryptString(ReadWritePrescriptionActivity.PRESC_STRING)
                try
                {
                    val MSG_ID_DEBUG: Byte = 0x58
                    val command1 = byteArrayOf(MSG_ID_DEBUG, MSG_DIRECTION_COMMAND,
                            PRESCRIPTION_ARRAY.get(0),
                            PRESCRIPTION_ARRAY.get(1),
                            PRESCRIPTION_ARRAY.get(2),
                            PRESCRIPTION_ARRAY.get(3),
                            PRESCRIPTION_ARRAY.get(4),
                            PRESCRIPTION_ARRAY.get(5),
                            PRESCRIPTION_ARRAY.get(6),
                            PRESCRIPTION_ARRAY.get(7),
                            PRESCRIPTION_ARRAY.get(8),
                            PRESCRIPTION_ARRAY.get(9),
                            PRESCRIPTION_ARRAY.get(10),
                            PRESCRIPTION_ARRAY.get(11),
                            PRESCRIPTION_ARRAY.get(12),
                            PRESCRIPTION_ARRAY.get(13),
                            PRESCRIPTION_ARRAY.get(14),
                            PRESCRIPTION_ARRAY.get(15),
                            PRESCRIPTION_ARRAY.get(16),
                            PRESCRIPTION_ARRAY.get(17),
                            PRESCRIPTION_ARRAY.get(18),
                            PRESCRIPTION_ARRAY.get(19),
                            PRESCRIPTION_ARRAY.get(20),
                            PRESCRIPTION_ARRAY.get(21),
                            PRESCRIPTION_ARRAY.get(22),
                            PRESCRIPTION_ARRAY.get(23),
                            PRESCRIPTION_ARRAY.get(24),
                            PRESCRIPTION_ARRAY.get(25),
                            PRESCRIPTION_ARRAY.get(26),
                            PRESCRIPTION_ARRAY.get(27),
                            PRESCRIPTION_ARRAY.get(28),
                            PRESCRIPTION_ARRAY.get(29),
                            PRESCRIPTION_ARRAY.get(30),
                            PRESCRIPTION_ARRAY.get(31),
                            PRESCRIPTION_ARRAY.get(32),
                            PRESCRIPTION_ARRAY.get(33),
                            PRESCRIPTION_ARRAY.get(34),
                            PRESCRIPTION_ARRAY.get(35),
                            PRESCRIPTION_ARRAY.get(36),
                            PRESCRIPTION_ARRAY.get(37),
                            PRESCRIPTION_ARRAY.get(38),
                            PRESCRIPTION_ARRAY.get(39),
                            PRESCRIPTION_ARRAY.get(40),
                            PRESCRIPTION_ARRAY.get(41),
                            PRESCRIPTION_ARRAY.get(42),
                            PRESCRIPTION_ARRAY.get(43),
                            PRESCRIPTION_ARRAY.get(44),
                            PRESCRIPTION_ARRAY.get(45),
                            PRESCRIPTION_ARRAY.get(46),
                            PRESCRIPTION_ARRAY.get(47),
                            PRESCRIPTION_ARRAY.get(48),
                            PRESCRIPTION_ARRAY.get(49),
                            PRESCRIPTION_ARRAY.get(50),
                            PRESCRIPTION_ARRAY.get(51),
                            PRESCRIPTION_ARRAY.get(52),
                            PRESCRIPTION_ARRAY.get(53),
                            PRESCRIPTION_ARRAY.get(54),
                            PRESCRIPTION_ARRAY.get(55),
                            PRESCRIPTION_ARRAY.get(56),
                            PRESCRIPTION_ARRAY.get(57),
                            PRESCRIPTION_ARRAY.get(58),
                            PRESCRIPTION_ARRAY.get(59),
                            PRESCRIPTION_ARRAY.get(60),
                            PRESCRIPTION_ARRAY.get(61),
                            PRESCRIPTION_ARRAY.get(62),
                            PRESCRIPTION_ARRAY.get(63),
                            PRESCRIPTION_ARRAY.get(64),
                            PRESCRIPTION_ARRAY.get(65),
                            PRESCRIPTION_ARRAY.get(66),
                            PRESCRIPTION_ARRAY.get(67),
                            PRESCRIPTION_ARRAY.get(68),
                            PRESCRIPTION_ARRAY.get(69),
                            PRESCRIPTION_ARRAY.get(70),
                            PRESCRIPTION_ARRAY.get(71),
                            PRESCRIPTION_ARRAY.get(72),
                            PRESCRIPTION_ARRAY.get(73),
                            PRESCRIPTION_ARRAY.get(74),
                            PRESCRIPTION_ARRAY.get(75),
                            PRESCRIPTION_ARRAY.get(76),
                            PRESCRIPTION_ARRAY.get(77),
                            PRESCRIPTION_ARRAY.get(78),
                            PRESCRIPTION_ARRAY.get(79),
                            PRESCRIPTION_ARRAY.get(80),
                            PRESCRIPTION_ARRAY.get(81),
                            PRESCRIPTION_ARRAY.get(82),
                            PRESCRIPTION_ARRAY.get(83),
                            PRESCRIPTION_ARRAY.get(84),
                            PRESCRIPTION_ARRAY.get(85),
                            PRESCRIPTION_ARRAY.get(86),
                            PRESCRIPTION_ARRAY.get(87),
                            PRESCRIPTION_ARRAY.get(88),
                            PRESCRIPTION_ARRAY.get(89),
                            PRESCRIPTION_ARRAY.get(90),
                            PRESCRIPTION_ARRAY.get(91),
                            PRESCRIPTION_ARRAY.get(92),
                            PRESCRIPTION_ARRAY.get(93),
                            PRESCRIPTION_ARRAY.get(94),
                            PRESCRIPTION_ARRAY.get(95),
                            PRESCRIPTION_ARRAY.get(96),
                            PRESCRIPTION_ARRAY.get(97),
                            PRESCRIPTION_ARRAY.get(98),
                            PRESCRIPTION_ARRAY.get(99),
                            PRESCRIPTION_ARRAY.get(100),
                            PRESCRIPTION_ARRAY.get(101),
                            PRESCRIPTION_ARRAY.get(102),
                            PRESCRIPTION_ARRAY.get(103),
                            PRESCRIPTION_ARRAY.get(104),
                            PRESCRIPTION_ARRAY.get(105),
                            PRESCRIPTION_ARRAY.get(106),
                            PRESCRIPTION_ARRAY.get(107),
                            PRESCRIPTION_ARRAY.get(108),
                            PRESCRIPTION_ARRAY.get(109),
                            PRESCRIPTION_ARRAY.get(110),
                            PRESCRIPTION_ARRAY.get(111),
                            PRESCRIPTION_ARRAY.get(112),
                            PRESCRIPTION_ARRAY.get(113),
                            PRESCRIPTION_ARRAY.get(114),
                            PRESCRIPTION_ARRAY.get(115),
                            PRESCRIPTION_ARRAY.get(116),
                            PRESCRIPTION_ARRAY.get(117),
                            PRESCRIPTION_ARRAY.get(118),
                            PRESCRIPTION_ARRAY.get(119),
                            PRESCRIPTION_ARRAY.get(120),
                            PRESCRIPTION_ARRAY.get(121),
                            PRESCRIPTION_ARRAY.get(122),
                            PRESCRIPTION_ARRAY.get(123),
                            PRESCRIPTION_ARRAY.get(124),
                            PRESCRIPTION_ARRAY.get(125),
                            PRESCRIPTION_ARRAY.get(126),
                            PRESCRIPTION_ARRAY.get(127),
                            PRESCRIPTION_ARRAY.get(128),
                            PRESCRIPTION_ARRAY.get(129),
                            PRESCRIPTION_ARRAY.get(130),
                            PRESCRIPTION_ARRAY.get(131),
                            PRESCRIPTION_ARRAY.get(132),
                            PRESCRIPTION_ARRAY.get(133),
                            PRESCRIPTION_ARRAY.get(134),
                            PRESCRIPTION_ARRAY.get(135),
                            PRESCRIPTION_ARRAY.get(136),
                            PRESCRIPTION_ARRAY.get(137),
                            PRESCRIPTION_ARRAY.get(138),
                            PRESCRIPTION_ARRAY.get(139),
                            PRESCRIPTION_ARRAY.get(140),
                            PRESCRIPTION_ARRAY.get(141),
                            PRESCRIPTION_ARRAY.get(142),
                            PRESCRIPTION_ARRAY.get(143),
                            PRESCRIPTION_ARRAY.get(144),
                            PRESCRIPTION_ARRAY.get(145),
                            PRESCRIPTION_ARRAY.get(146),
                            PRESCRIPTION_ARRAY.get(147),
                            PRESCRIPTION_ARRAY.get(148),
                            PRESCRIPTION_ARRAY.get(149),
                            PRESCRIPTION_ARRAY.get(150),
                            PRESCRIPTION_ARRAY.get(151),
                            PRESCRIPTION_ARRAY.get(152),
                            PRESCRIPTION_ARRAY.get(153),
                            PRESCRIPTION_ARRAY.get(154),
                            PRESCRIPTION_ARRAY.get(155),
                            PRESCRIPTION_ARRAY.get(156),
                            PRESCRIPTION_ARRAY.get(157),
                            PRESCRIPTION_ARRAY.get(158),
                            PRESCRIPTION_ARRAY.get(159),
                            PRESCRIPTION_ARRAY.get(160),
                            PRESCRIPTION_ARRAY.get(161),
                            PRESCRIPTION_ARRAY.get(162),
                            PRESCRIPTION_ARRAY.get(163),
                            PRESCRIPTION_ARRAY.get(164),
                            PRESCRIPTION_ARRAY.get(165),
                            PRESCRIPTION_ARRAY.get(166),
                            PRESCRIPTION_ARRAY.get(167),
                            PRESCRIPTION_ARRAY.get(168),
                            PRESCRIPTION_ARRAY.get(169),
                            PRESCRIPTION_ARRAY.get(170),
                            PRESCRIPTION_ARRAY.get(171),
                            PRESCRIPTION_ARRAY.get(172),
                            PRESCRIPTION_ARRAY.get(173),
                            PRESCRIPTION_ARRAY.get(174),
                            PRESCRIPTION_ARRAY.get(175),
                            PRESCRIPTION_ARRAY.get(176),
                            PRESCRIPTION_ARRAY.get(177),
                            PRESCRIPTION_ARRAY.get(178),
                            PRESCRIPTION_ARRAY.get(179),
                            PRESCRIPTION_ARRAY.get(180),
                            PRESCRIPTION_ARRAY.get(181),
                            PRESCRIPTION_ARRAY.get(182),
                            PRESCRIPTION_ARRAY.get(183),
                            PRESCRIPTION_ARRAY.get(184),
                            PRESCRIPTION_ARRAY.get(185),
                            PRESCRIPTION_ARRAY.get(186),
                            PRESCRIPTION_ARRAY.get(187),
                            PRESCRIPTION_ARRAY.get(188),
                            PRESCRIPTION_ARRAY.get(189),
                            PRESCRIPTION_ARRAY.get(190),
                            PRESCRIPTION_ARRAY.get(191),
                            PRESCRIPTION_ARRAY.get(192),
                            PRESCRIPTION_ARRAY.get(193),
                            PRESCRIPTION_ARRAY.get(194),
                            PRESCRIPTION_ARRAY.get(195),
                            PRESCRIPTION_ARRAY.get(196),
                            PRESCRIPTION_ARRAY.get(197),
                            PRESCRIPTION_ARRAY.get(198),
                            PRESCRIPTION_ARRAY.get(199),
                            PRESCRIPTION_ARRAY.get(200),
                            PRESCRIPTION_ARRAY.get(201),
                            PRESCRIPTION_ARRAY.get(202),
                            PRESCRIPTION_ARRAY.get(203),
                            PRESCRIPTION_ARRAY.get(204),
                            PRESCRIPTION_ARRAY.get(205),
                            PRESCRIPTION_ARRAY.get(206),
                            PRESCRIPTION_ARRAY.get(207),
                            PRESCRIPTION_ARRAY.get(208),
                            PRESCRIPTION_ARRAY.get(209),
                            PRESCRIPTION_ARRAY.get(210),
                            PRESCRIPTION_ARRAY.get(211),
                            PRESCRIPTION_ARRAY.get(212),
                            PRESCRIPTION_ARRAY.get(213),
                            PRESCRIPTION_ARRAY.get(214),
                            PRESCRIPTION_ARRAY.get(215),
                            PRESCRIPTION_ARRAY.get(216),
                            PRESCRIPTION_ARRAY.get(217),
                            PRESCRIPTION_ARRAY.get(218),
                            PRESCRIPTION_ARRAY.get(219),
                            PRESCRIPTION_ARRAY.get(220),
                            PRESCRIPTION_ARRAY.get(221),
                            PRESCRIPTION_ARRAY.get(222),
                            PRESCRIPTION_ARRAY.get(223),
                            PRESCRIPTION_ARRAY.get(224),
                            PRESCRIPTION_ARRAY.get(225),
                            PRESCRIPTION_ARRAY.get(226),
                            PRESCRIPTION_ARRAY.get(227),
                            PRESCRIPTION_ARRAY.get(228),
                            PRESCRIPTION_ARRAY.get(229),
                            PRESCRIPTION_ARRAY.get(230),
                            PRESCRIPTION_ARRAY.get(231),
                            PRESCRIPTION_ARRAY.get(232),
                            PRESCRIPTION_ARRAY.get(233),
                            PRESCRIPTION_ARRAY.get(234),
                            PRESCRIPTION_ARRAY.get(235),
                            PRESCRIPTION_ARRAY.get(236),
                            PRESCRIPTION_ARRAY.get(237),
                            PRESCRIPTION_ARRAY.get(238),
                            PRESCRIPTION_ARRAY.get(239),
                            PRESCRIPTION_ARRAY.get(240),
                            PRESCRIPTION_ARRAY.get(241),
                            PRESCRIPTION_ARRAY.get(242),
                            PRESCRIPTION_ARRAY.get(243),
                            PRESCRIPTION_ARRAY.get(244),
                            PRESCRIPTION_ARRAY.get(245),
                            PRESCRIPTION_ARRAY.get(246),
                            PRESCRIPTION_ARRAY.get(247),
                            PRESCRIPTION_ARRAY.get(248),
                            PRESCRIPTION_ARRAY.get(249),
                            PRESCRIPTION_ARRAY.get(250),
                            PRESCRIPTION_ARRAY.get(251),
                            PRESCRIPTION_ARRAY.get(252),
                            PRESCRIPTION_ARRAY.get(253),
                            PRESCRIPTION_ARRAY.get(254),
                            PRESCRIPTION_ARRAY.get(255),
                            PRESCRIPTION_ARRAY.get(256),
                            PRESCRIPTION_ARRAY.get(257),
                            PRESCRIPTION_ARRAY.get(258),
                            PRESCRIPTION_ARRAY.get(259),
                            PRESCRIPTION_ARRAY.get(260),
                            PRESCRIPTION_ARRAY.get(261),
                            PRESCRIPTION_ARRAY.get(262),
                            PRESCRIPTION_ARRAY.get(263),
                            PRESCRIPTION_ARRAY.get(264),
                            PRESCRIPTION_ARRAY.get(265),
                            PRESCRIPTION_ARRAY.get(266),
                            PRESCRIPTION_ARRAY.get(267),
                            PRESCRIPTION_ARRAY.get(268),
                            PRESCRIPTION_ARRAY.get(269),
                            PRESCRIPTION_ARRAY.get(270),
                            PRESCRIPTION_ARRAY.get(271),
                            PRESCRIPTION_ARRAY.get(272),
                            PRESCRIPTION_ARRAY.get(273),
                            PRESCRIPTION_ARRAY.get(274),
                            PRESCRIPTION_ARRAY.get(275),
                            PRESCRIPTION_ARRAY.get(276),
                            PRESCRIPTION_ARRAY.get(277),
                            PRESCRIPTION_ARRAY.get(278),
                            PRESCRIPTION_ARRAY.get(279),
                            PRESCRIPTION_ARRAY.get(280),
                            PRESCRIPTION_ARRAY.get(281),
                            PRESCRIPTION_ARRAY.get(282),
                            PRESCRIPTION_ARRAY.get(283),
                            PRESCRIPTION_ARRAY.get(284),
                            PRESCRIPTION_ARRAY.get(285),
                            PRESCRIPTION_ARRAY.get(286),
                            PRESCRIPTION_ARRAY.get(287),
                            PRESCRIPTION_ARRAY.get(288),
                            PRESCRIPTION_ARRAY.get(289),
                            PRESCRIPTION_ARRAY.get(290),
                            PRESCRIPTION_ARRAY.get(291),
                            PRESCRIPTION_ARRAY.get(292),
                            PRESCRIPTION_ARRAY.get(293),
                            PRESCRIPTION_ARRAY.get(294),
                            PRESCRIPTION_ARRAY.get(295),
                            PRESCRIPTION_ARRAY.get(296),
                            PRESCRIPTION_ARRAY.get(297),
                            PRESCRIPTION_ARRAY.get(298),
                            PRESCRIPTION_ARRAY.get(299),
                            PRESCRIPTION_ARRAY.get(300),
                            PRESCRIPTION_ARRAY.get(301),
                            PRESCRIPTION_ARRAY.get(302),
                            PRESCRIPTION_ARRAY.get(303),
                            PRESCRIPTION_ARRAY.get(304),
                            PRESCRIPTION_ARRAY.get(305),
                            PRESCRIPTION_ARRAY.get(306),
                            PRESCRIPTION_ARRAY.get(307),
                            PRESCRIPTION_ARRAY.get(308),
                            PRESCRIPTION_ARRAY.get(309),
                            PRESCRIPTION_ARRAY.get(310),
                            PRESCRIPTION_ARRAY.get(311),
                            PRESCRIPTION_ARRAY.get(312),
                            PRESCRIPTION_ARRAY.get(313),
                            PRESCRIPTION_ARRAY.get(314),
                            PRESCRIPTION_ARRAY.get(315),
                            PRESCRIPTION_ARRAY.get(316),
                            PRESCRIPTION_ARRAY.get(317),
                            PRESCRIPTION_ARRAY.get(318),
                            PRESCRIPTION_ARRAY.get(319),
                            PRESCRIPTION_ARRAY.get(320),
                            PRESCRIPTION_ARRAY.get(321),
                            PRESCRIPTION_ARRAY.get(322),
                            PRESCRIPTION_ARRAY.get(323),
                            PRESCRIPTION_ARRAY.get(324),
                            PRESCRIPTION_ARRAY.get(325),
                            PRESCRIPTION_ARRAY.get(326),
                            PRESCRIPTION_ARRAY.get(327),
                            PRESCRIPTION_ARRAY.get(328),
                            PRESCRIPTION_ARRAY.get(329),
                            PRESCRIPTION_ARRAY.get(330),
                            PRESCRIPTION_ARRAY.get(331),
                            PRESCRIPTION_ARRAY.get(332),
                            PRESCRIPTION_ARRAY.get(333),
                            PRESCRIPTION_ARRAY.get(334),
                            PRESCRIPTION_ARRAY.get(335),
                            PRESCRIPTION_ARRAY.get(336),
                            PRESCRIPTION_ARRAY.get(337),
                            PRESCRIPTION_ARRAY.get(338),
                            PRESCRIPTION_ARRAY.get(339),
                            PRESCRIPTION_ARRAY.get(340),
                            PRESCRIPTION_ARRAY.get(341),
                            PRESCRIPTION_ARRAY.get(342),
                            PRESCRIPTION_ARRAY.get(343),
                            PRESCRIPTION_ARRAY.get(344),
                            PRESCRIPTION_ARRAY.get(345),
                            PRESCRIPTION_ARRAY.get(346),
                            PRESCRIPTION_ARRAY.get(347),
                            PRESCRIPTION_ARRAY.get(348),
                            PRESCRIPTION_ARRAY.get(349),
                            PRESCRIPTION_ARRAY.get(350),
                            PRESCRIPTION_ARRAY.get(351),
                            PRESCRIPTION_ARRAY.get(352),
                            PRESCRIPTION_ARRAY.get(353),
                            PRESCRIPTION_ARRAY.get(354),
                            PRESCRIPTION_ARRAY.get(355),
                            PRESCRIPTION_ARRAY.get(356),
                            PRESCRIPTION_ARRAY.get(357),
                            PRESCRIPTION_ARRAY.get(358),
                            PRESCRIPTION_ARRAY.get(359),
                            PRESCRIPTION_ARRAY.get(360),
                            PRESCRIPTION_ARRAY.get(361),
                            PRESCRIPTION_ARRAY.get(362),
                            PRESCRIPTION_ARRAY.get(363),
                            PRESCRIPTION_ARRAY.get(364),
                            PRESCRIPTION_ARRAY.get(365),
                            PRESCRIPTION_ARRAY.get(366),
                            PRESCRIPTION_ARRAY.get(367),
                            PRESCRIPTION_ARRAY.get(368),
                            PRESCRIPTION_ARRAY.get(369),
                            PRESCRIPTION_ARRAY.get(370),
                            PRESCRIPTION_ARRAY.get(371),
                            PRESCRIPTION_ARRAY.get(372),
                            PRESCRIPTION_ARRAY.get(373),
                            PRESCRIPTION_ARRAY.get(374))


                    //Log.e("ENCRYPT DECRYPT","command1 : "+command1)
                    val response = writeReadNdef(command1)
                    if(response!!.get(0).size > 0)
                    {
                        listner.writePrescriptionSuccess("Prescription write successfully")
                    }
                    else
                    {
                        listner.writePrescriptionFailure("Fail to write Prescription")
                    }
                    //listner.notifyUser("Size : " + response!!.get(0).size + " Response : " + Arrays.toString(response.get(0)))
                }
                catch (e : Exception)
                {
                    listner.writePrescriptionFailure("Fail to write Prescription ")
                }
            }
            else
            {
                if(Arrays.toString(responses[0]).equals("[87, 1, 15, 0, 1, 0]"))
                {
                    F.t("Pillpack not connected")
                }
                else
                {
                    F.t("Pillpack not connected error")
                }
                /*Log.e("NDEF_RESPONSE", "responses : "+Arrays.toString(responses[0]))

                listner.notifyUser("Error. Unexpected response: " + Arrays.toString(responses[0]))

                Log.e("NDEF_RESPONSE", "errorCode : "+errorCode)*/
            }
        }
        //var sdf = checkResponse(writeReadNdef(command), byteArrayOf(MSG_ID_START, MSG_DIRECTION_RESPONSE, 0, 0, 0, 0), "Therapy started")



    }

    private fun writeReadNdef(payload: ByteArray): List<ByteArray>? {
        var responses: List<ByteArray>? = null
        try {
            ReadWritePrescriptionActivity.ndef!!.connect()
            try {
                writeNdef(payload)
                try { /* Give the NHS31xx some time to respond. */
                    //Thread.sleep(111);
                    Thread.sleep(250)
                } catch (e: InterruptedException) {
                    Log.e("sleep", e.toString())
                }

                responses = readNdef()
            } finally {
                ReadWritePrescriptionActivity.ndef!!.close()
            }
        } catch (e: NullPointerException) {
            Log.e("writeReadNdef", e.toString())
        } catch (e: IOException) {
            Log.e("writeReadNdef", e.toString())
        } catch (e: IllegalStateException) {
            Log.e("writeReadNdef", e.toString())
        }

        return responses
    }

    @Throws(IOException::class)
    private fun readNdef(): List<ByteArray> {
        val responses = ArrayList<ByteArray>()
        try {
            val ndefMessage = ReadWritePrescriptionActivity.ndef!!.getNdefMessage()
            for (r in ndefMessage.getRecords()) {
                val response = r.getPayload()
                if (r.getTnf().toInt() != 2) {
                    Log.d("readNdef", "Skipping a non-mime record")
                } else if (response == null || response!!.size < 2) {
                    Log.i("readNdef", "Empty record or record too short. Ignored.")
                } else if (response!![1].toInt() != 1) {
                    Log.d("readNdef", "Tag byte not 1. Did we read back our own command?")
                } else {
                    responses.add(response)
                }
            }
        } catch (e: FormatException) {
            Log.i("readNdef", e.toString())
        }

        return responses
    }

    @Throws(IOException::class)
    private fun writeNdef(payload: ByteArray) {
        val record = android.nfc.NdefRecord.createMime("n/p", payload)
        val message = NdefMessage(arrayOf(record))
        try {

            Log.d("writeNdef", "MESSAGE : " + message.toString())
            ReadWritePrescriptionActivity.ndef!!.writeNdefMessage(message)
        } catch (e: FormatException) {
            Log.e("writeNdef", e.toString())
        }

    }

    private fun checkResponse(responses: List<ByteArray>?, expected: ByteArray, success: String)
    {
        if (responses == null)
        {
            listner.notifyUser("Error sending command. Re-touch correct tag.")

        }
        else if (responses.isEmpty())
        {
            listner.notifyUser("Error. No response received.")
        }
        else
        {
            if (Arrays.equals(responses[0], expected))
            {
                listner.notifyUser(success)
            }
            else
            {
                listner.notifyUser("Error. Unexpected response: " + Arrays.toString(responses[0]))
            }
        }
    }
}