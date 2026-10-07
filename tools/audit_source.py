#!/usr/bin/env python3
"""Compare verified method signatures and normalized instructions with May bytecode.

This deliberately audits a named subset, not speculative whole-program symbol matches.
Different instruction sequences do not imply different behavior (see test_source.py).
"""
import hashlib
import json
import os
import re
import recover

ROOT = recover.ROOT
OUT = ROOT / 'build/source-audit'
CLASSES = {'co': 'jimm/comm/Util', 'cj': 'jimm/Options', 'z': 'jimm/ContactItem',
           'ci': 'DrawControls/VirtualAlert', 'cf': 'jimm/JimmUI',
           'ag': 'jimm/MainMenu', 'aq': 'jimm/Templates',
           'r': 'jimm/comm/Icq', 'n': 'jimm/comm/ConnectAction',
           'ap': 'jimm/comm/Icq$Connection', 'bv': 'jimm/JimmException',
           'cv': 'jimm/SplashCanvas', 'cd': 'DrawControls/VirtualList',
           'aa': 'jimm/comm/Action', 'at': 'jimm/TimerTasks', 'e': 'DrawControls/Icon',
           'ay': 'jimm/comm/Icq$HTTPConnection', 'cb': 'jimm/comm/Icq$SOCKSConnection',
           'an': 'jimm/comm/Packet', 'ct': 'jimm/comm/UpdateContactListAction',
           'bq': 'jimm/comm/OtherAction', 'ae': 'jimm/comm/ActionListener',
           'ac': 'jimm/comm/Message', 'ah': 'jimm/util/MagicEye', 'br': 'jimm/EditInfo', 'as': 'jimm/comm/SaveInfoAction',
           'ce': 'jimm/comm/RequestInfoAction', 'bi': 'DrawControls/TextList',
           'g': 'jimm/JimmUI$GetVersionInfoTimerTask', 'm': 'jimm/ContactList', 'cp': 'jimm/Search',
           'cr': 'jimm/Search$SearchForm', 'o': 'jimm/comm/SearchAction',
           'p': 'jimm/FileTransfer', 'u': 'jimm/FileBrowser', 'x': 'jimm/Traffic',
           'cc': 'jimm/FileTransfer$ViewFinder', 'd': 'DrawControls/VirtualForm',
           'j': 'DrawControls/FormChoiceGroup', 'k': 'jimm/FileBrowserListener',
           'aw': 'DrawControls/VirtualTree', 'ax': 'DrawControls/TreeNode', 'cn': 'DrawControls/ListItem',
           'ab': 'jimm/comm/FileTransferMessage', 'bw': 'jimm/comm/DirectConnectionAction',
           'ao': 'jimm/comm/Icq$PeerConnection', 'au': 'jimm/comm/DCPacket',
           'cl': 'jimm/comm/SendMessageAction', 'av': 'jimm/comm/PlainMessage',
           'a': 'jimm/Traffic$TrafficScreen', 'bl': 'jimm/XStatusForm', 'af': 'jimm/util/Selector', 'y': 'jimm/ChatTextList', 'bt': 'jimm/ChatHistory', 'ca': 'jimm/MessData', 'bo': 'jimm/Emotions'}
CLASSES.update({'q': 'jimm/HistoryStorage', 'bg': 'jimm/HistoryStorageList', 'bz': 'jimm/CachedRecord', 'w': 'jimm/FileSystem'})
SYMBOLS = {
    'q': 'jimm/HistoryStorage',
    'bg': 'jimm/HistoryStorageList',
    'bz': 'jimm/CachedRecord',
    'w': 'jimm/FileSystem',
    'q.<init>()V': 'jimm/HistoryStorage.<init>()V',
    'q.<clinit>()V': 'jimm/HistoryStorage.<clinit>()V',
    'q.a(Ljava/lang/String;Ljava/lang/String;BLjava/lang/String;J)V': 'jimm/HistoryStorage.addText(Ljava/lang/String;Ljava/lang/String;BLjava/lang/String;J)V',
    'q.a()Ljavax/microedition/rms/RecordStore;': 'jimm/HistoryStorage.getRS()Ljavax/microedition/rms/RecordStore;',
    'q.a(Ljava/lang/String;)Ljava/lang/String;': 'jimm/HistoryStorage.getRSName(Ljava/lang/String;)Ljava/lang/String;',
    'q.c(Ljava/lang/String;)V': 'jimm/HistoryStorage.openUINRecords(Ljava/lang/String;)V',
    'q.a(Ljava/lang/String;)I': 'jimm/HistoryStorage.getRecordCount(Ljava/lang/String;)I',
    'q.a(Ljava/lang/String;I)Lbz;': 'jimm/HistoryStorage.getRecord(Ljava/lang/String;I)Ljimm/CachedRecord;',
    'q.b(Ljava/lang/String;I)Lbz;': 'jimm/HistoryStorage.getCachedRecord(Ljava/lang/String;I)Ljimm/CachedRecord;',
    'q.a(Ljava/lang/String;Ljava/lang/String;)V': 'jimm/HistoryStorage.showHistoryList(Ljava/lang/String;Ljava/lang/String;)V',
    'q.a(Ljava/lang/String;)V': 'jimm/HistoryStorage.clearHistory(Ljava/lang/String;)V',
    'q.a()V': 'jimm/HistoryStorage.clearCache()V',
    'q.b()V': 'jimm/HistoryStorage.setColorScheme()V',
    'q.a(Ljava/lang/String;Ljava/lang/String;ZZ)Z': 'jimm/HistoryStorage.find_intern(Ljava/lang/String;Ljava/lang/String;ZZ)Z',
    'q.a(Ljava/lang/String;Ljava/lang/String;ZZ)V': 'jimm/HistoryStorage.find(Ljava/lang/String;Ljava/lang/String;ZZ)V',
    'q.b(Ljava/lang/String;)V': 'jimm/HistoryStorage.clear_all(Ljava/lang/String;)V',
    'bg.<init>()V': 'jimm/HistoryStorageList.<init>()V',
    'bg.<clinit>()V': 'jimm/HistoryStorageList.<clinit>()V',
    'bg.a(Lcd;)V': 'jimm/HistoryStorageList.vlCursorMoved(LDrawControls/VirtualList;)V',
    'bg.a(Lcd;II)V': 'jimm/HistoryStorageList.vlKeyPress(LDrawControls/VirtualList;II)V',
    'bg.d(Z)V': 'jimm/HistoryStorageList.copyText(Z)V',
    'bg.b(Lcd;)V': 'jimm/HistoryStorageList.vlItemClicked(LDrawControls/VirtualList;)V',
    'bg.a(I)V': 'jimm/HistoryStorageList.moveInList(I)V',
    'bg.a()Lz;': 'jimm/HistoryStorageList.getCItem()Ljimm/ContactItem;',
    'bg.d(Ljava/lang/String;)V': 'jimm/HistoryStorageList.export(Ljava/lang/String;)V',
    'bg.a(Ljava/lang/String;)V': 'jimm/HistoryStorageList.onFileSelect(Ljava/lang/String;)V',
    'bg.b(Ljava/lang/String;)V': 'jimm/HistoryStorageList.onDirectorySelect(Ljava/lang/String;)V',
    'bg.run()V': 'jimm/HistoryStorageList.run()V',
    'bg.a([Lz;)V': 'jimm/HistoryStorageList.startExport([Ljimm/ContactItem;)V',
    'bg.a(Ljava/lang/String;)Lw;': 'jimm/HistoryStorageList.openFile(Ljava/lang/String;)Ljimm/FileSystem;',
    'bg.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V': 'jimm/HistoryStorageList.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V',
    'bg.a()V': 'jimm/HistoryStorageList.showMessText()V',
    'bg.a()Ljava/lang/String;': 'jimm/HistoryStorageList.getCurrUin()Ljava/lang/String;',
    'bg.a$16da05f7(Ljava/lang/String;)V': 'jimm/HistoryStorageList.setCurrUin$16da05f7(Ljava/lang/String;)V',
    'bg.a()I': 'jimm/HistoryStorageList.getSize()I',
    'bg.a(ILcn;)V': 'jimm/HistoryStorageList.get(ILDrawControls/ListItem;)V',
    'q.a Ljavax/microedition/rms/RecordStore;': 'jimm/HistoryStorage.recordStore Ljavax/microedition/rms/RecordStore;',
    'q.a Lbg;': 'jimm/HistoryStorage.list Ljimm/HistoryStorageList;',
    'q.a Ljava/lang/String;': 'jimm/HistoryStorage.currCacheUin Ljava/lang/String;',
    'q.a Ljava/util/Hashtable;': 'jimm/HistoryStorage.cachedRecords Ljava/util/Hashtable;',
    'bg.c Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdMsgNext Ljavax/microedition/lcdui/Command;',
    'bg.d Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdMsgPrev Ljavax/microedition/lcdui/Command;',
    'bg.e Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdBack Ljavax/microedition/lcdui/Command;',
    'bg.f Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdClear Ljavax/microedition/lcdui/Command;',
    'bg.g Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdInfo Ljavax/microedition/lcdui/Command;',
    'bg.h Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdExport Ljavax/microedition/lcdui/Command;',
    'bg.i Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdExportAll Ljavax/microedition/lcdui/Command;',
    'bg.j Ljavax/microedition/lcdui/Command;': 'jimm/HistoryStorageList.cmdGotoURL Ljavax/microedition/lcdui/Command;',
    'bg.a Lbi;': 'jimm/HistoryStorageList.messText LDrawControls/TextList;',
    'bg.a Ljava/lang/String;': 'jimm/HistoryStorageList.currUin Ljava/lang/String;',
    'bg.a Ld;': 'jimm/HistoryStorageList.frmFind LDrawControls/VirtualForm;',
    'bg.a Ljavax/microedition/lcdui/TextField;': 'jimm/HistoryStorageList.tfldFind Ljavax/microedition/lcdui/TextField;',
    'bg.a Lj;': 'jimm/HistoryStorageList.chsFind LDrawControls/FormChoiceGroup;',
    'bg.b Z': 'jimm/HistoryStorageList.cp1251 Z',
    'bg.b Ljava/lang/String;': 'jimm/HistoryStorageList.exportUin Ljava/lang/String;',
    'bg.c Ljava/lang/String;': 'jimm/HistoryStorageList.directory Ljava/lang/String;',
    'bg.b Lbi;': 'jimm/HistoryStorageList.URLList LDrawControls/TextList;',
    'bz.a Ljava/lang/String;': 'jimm/CachedRecord.shortText Ljava/lang/String;',
    'bz.b Ljava/lang/String;': 'jimm/CachedRecord.text Ljava/lang/String;',
    'bz.c Ljava/lang/String;': 'jimm/CachedRecord.date Ljava/lang/String;',
    'bz.d Ljava/lang/String;': 'jimm/CachedRecord.from Ljava/lang/String;',
    'bz.a B': 'jimm/CachedRecord.type B',
    'bz.a Z': 'jimm/CachedRecord.contains_url Z',

    'y': 'jimm/ChatTextList',
    'bt': 'jimm/ChatHistory',
    'ca': 'jimm/MessData',
    'bo': 'jimm/Emotions',
    'y.a(Lz;)V': 'jimm/ChatTextList.buildMenu(Ljimm/ContactItem;)V',
    'y.a()Z': 'jimm/ChatTextList.isVisible()Z',
    'y.a()V': 'jimm/ChatTextList.removeAuthCommands()V',
    'y.a(Z)I': 'jimm/ChatTextList.getInOutColor(Z)I',
    'y.a(Lcd;)V': 'jimm/ChatTextList.vlCursorMoved(LDrawControls/VirtualList;)V',
    'y.b()V': 'jimm/ChatTextList.checkTextForURL()V',
    'y.c()V': 'jimm/ChatTextList.checkForAuthReply()V',
    'y.b(Lcd;)V': 'jimm/ChatTextList.vlItemClicked(LDrawControls/VirtualList;)V',
    'y.a(Lcd;II)V': 'jimm/ChatTextList.vlKeyPress(LDrawControls/VirtualList;II)V',
    'y.e()V': 'jimm/ChatTextList.activate()V',
    'y.<init>(Ljava/lang/String;Lz;)V': 'jimm/ChatTextList.<init>(Ljava/lang/String;Ljimm/ContactItem;)V',
    'y.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V': 'jimm/ChatTextList.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V',
    'y.<clinit>()V': 'jimm/ChatTextList.<clinit>()V',
    'bt.a(Lz;Lac;)V': 'jimm/ChatHistory.addMessage(Ljimm/ContactItem;Ljimm/comm/Message;)V',
    'bt.a(Ljava/lang/String;JZ)V': 'jimm/ChatHistory.AckMessage(Ljava/lang/String;JZ)V',
    'bt.a(Lz;Ljava/lang/String;JLjava/lang/String;J)V': 'jimm/ChatHistory.addMyMessage(Ljimm/ContactItem;Ljava/lang/String;JLjava/lang/String;J)V',
    'bt.a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZZLe;J)V': 'jimm/ChatHistory.addTextToForm(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZZLDrawControls/Icon;J)V',
    'bt.a(Ljava/lang/String;Ljava/lang/String;)V': 'jimm/ChatHistory.copyText(Ljava/lang/String;Ljava/lang/String;)V',
    'bt.a(Ljava/lang/String;)Ly;': 'jimm/ChatHistory.getChatHistoryAt(Ljava/lang/String;)Ljimm/ChatTextList;',
    'bt.a(Ljava/lang/String;)V': 'jimm/ChatHistory.chatHistoryDelete(Ljava/lang/String;)V',
    'bt.a(Ljava/lang/String;I)V': 'jimm/ChatHistory.chatHistoryDelete(Ljava/lang/String;I)V',
    'bt.a(Ljava/lang/String;)Z': 'jimm/ChatHistory.chatHistoryShown(Ljava/lang/String;)Z',
    'bt.b(Ljava/lang/String;)Z': 'jimm/ChatHistory.chatHistoryExists(Ljava/lang/String;)Z',
    'bt.a(Lz;Ljava/lang/String;)V': 'jimm/ChatHistory.newChatForm(Ljimm/ContactItem;Ljava/lang/String;)V',
    'bt.a(Lz;)V': 'jimm/ChatHistory.fillFormHistory(Ljimm/ContactItem;)V',
    'bt.b(Ljava/lang/String;Ljava/lang/String;)V': 'jimm/ChatHistory.contactRenamed(Ljava/lang/String;Ljava/lang/String;)V',
    'bt.b(Ljava/lang/String;)V': 'jimm/ChatHistory.UpdateCaption(Ljava/lang/String;)V',
    'bt.a()V': 'jimm/ChatHistory.setColorScheme()V',
    'bt.c(Ljava/lang/String;)V': 'jimm/ChatHistory.calcCounter(Ljava/lang/String;)V',
    'bt.a(Lz;)Z': 'jimm/ChatHistory.activateIfExists(Ljimm/ContactItem;)Z',
    'bt.b(Lz;)V': 'jimm/ChatHistory.removeAuthCommands(Ljimm/ContactItem;)V',
    'bt.<init>()V': 'jimm/ChatHistory.<init>()V',
    'bt.<clinit>()V': 'jimm/ChatHistory.<clinit>()V',
    'ca.a()Z': 'jimm/MessData.getIncoming()Z',
    'ca.<init>(ZJIZ)V': 'jimm/MessData.<init>(ZJIZ)V',
    'bo.a(Ljava/util/Vector;Ljava/lang/String;Ljava/lang/Integer;)V': 'jimm/Emotions.insertTextCorr(Ljava/util/Vector;Ljava/lang/String;Ljava/lang/Integer;)V',
    'bo.a(Ljava/io/DataInputStream;)Ljava/lang/String;': 'jimm/Emotions.readLineFromStream(Ljava/io/DataInputStream;)Ljava/lang/String;',
    'bo.a(Lbi;Ljava/lang/String;III)V': 'jimm/Emotions.addTextWithEmotions(LDrawControls/TextList;Ljava/lang/String;III)V',
    'bo.a(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V': 'jimm/Emotions.selectEmotion(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V',
    'bo.a(Lcd;II)V': 'jimm/Emotions.vlKeyPress(LDrawControls/VirtualList;II)V',
    'bo.a(Lcd;)V': 'jimm/Emotions.vlCursorMoved(LDrawControls/VirtualList;)V',
    'bo.b(Lcd;)V': 'jimm/Emotions.vlItemClicked(LDrawControls/VirtualList;)V',
    'bo.a()V': 'jimm/Emotions.select()V',
    'bo.<init>()V': 'jimm/Emotions.<init>()V',
    'bo.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V': 'jimm/Emotions.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V',
    'bo.<clinit>()V': 'jimm/Emotions.<clinit>()V',
    'y.a Lbi;': 'jimm/ChatTextList.textList LDrawControls/TextList;',
    'y.a Ljava/lang/String;': 'jimm/ChatTextList.ChatName Ljava/lang/String;',
    'y.a Lz;': 'jimm/ChatTextList.contact Ljimm/ContactItem;',
    'y.a Ljava/util/Vector;': 'jimm/ChatTextList.messData Ljava/util/Vector;',
    'y.a I': 'jimm/ChatTextList.messTotalCounter I',
    'bt.a Ljava/util/Hashtable;': 'jimm/ChatHistory.historyTable Ljava/util/Hashtable;',
    'bt.a I': 'jimm/ChatHistory.counter I',
    'bt.a Le;': 'jimm/ChatHistory.image LDrawControls/Icon;',
    'ca.a J': 'jimm/MessData.time J',
    'ca.a I': 'jimm/MessData.rowData I',
    'bo.a [I': 'jimm/Emotions.selEmotionsIndexes [I',
    'bo.b [I': 'jimm/Emotions.textCorrIndexes [I',
    'bo.a [Ljava/lang/String;': 'jimm/Emotions.selEmotionsWord [Ljava/lang/String;',
    'bo.b [Ljava/lang/String;': 'jimm/Emotions.selEmotionsSmileNames [Ljava/lang/String;',
    'bo.c [Ljava/lang/String;': 'jimm/Emotions.textCorrWords [Ljava/lang/String;',
    'bo.a [Z': 'jimm/Emotions.emoFinded [Z',
    'bo.a I': 'jimm/Emotions.lastSelectedEmotion I',
    'bo.b I': 'jimm/Emotions.caretPos I',
    'bo.a Ljava/lang/String;': 'jimm/Emotions.emotionText Ljava/lang/String;',
    'bo.a Ljava/lang/Object;': 'jimm/Emotions.lastScreen Ljava/lang/Object;',
    'bo.a Ljavax/microedition/lcdui/TextBox;': 'jimm/Emotions.textBox Ljavax/microedition/lcdui/TextBox;',
    'bo.a Z': 'jimm/Emotions.used Z',
    'bo.a Ljava/util/Vector;': 'jimm/Emotions.findedEmotions Ljava/util/Vector;',
    'y.a Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdContactMenu Ljavax/microedition/lcdui/Command;',
    'y.b Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdMsgReply Ljavax/microedition/lcdui/Command;',
    'y.c Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdCloseChat Ljavax/microedition/lcdui/Command;',
    'y.d Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdDenyAuth Ljavax/microedition/lcdui/Command;',
    'y.e Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdReqAuth Ljavax/microedition/lcdui/Command;',
    'y.f Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdGrantAuth Ljavax/microedition/lcdui/Command;',
    'y.g Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdAddUrs Ljavax/microedition/lcdui/Command;',
    'y.h Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdAddToHistory Ljavax/microedition/lcdui/Command;',
    'y.i Ljavax/microedition/lcdui/Command;': 'jimm/ChatTextList.cmdDelChat Ljavax/microedition/lcdui/Command;',

    'bl': 'jimm/XStatusForm',
    'af': 'jimm/util/Selector',
    'bl.a Ld;': 'jimm/XStatusForm.form LDrawControls/VirtualForm;',
    'bl.a Ljavax/microedition/lcdui/TextField;': 'jimm/XStatusForm.titleTextField Ljavax/microedition/lcdui/TextField;',
    'bl.b Ljavax/microedition/lcdui/TextField;': 'jimm/XStatusForm.descTextField Ljavax/microedition/lcdui/TextField;',
    'bl.a Ljavax/microedition/lcdui/ChoiceGroup;': 'jimm/XStatusForm.choiceGroup Ljavax/microedition/lcdui/ChoiceGroup;',
    'bl.a Ljava/util/Vector;': 'jimm/XStatusForm.xstatusform Ljava/util/Vector;',
    'bl.a I': 'jimm/XStatusForm.xstIndex I',
    'bl.a Z': 'jimm/XStatusForm.happyFlag Z',
    'bl.<init>()V': 'jimm/XStatusForm.<init>()V',
    'bl.a(I)Ljava/lang/String;': 'jimm/XStatusForm.getRecordDesc(I)Ljava/lang/String;',
    'bl.a()Ljava/lang/String;': 'jimm/XStatusForm.saveInLine()Ljava/lang/String;',
    'bl.a([B)V': 'jimm/XStatusForm.LoadLineInTable([B)V',
    'ag.d()V': 'jimm/MainMenu.showXStatusSelector()V',
    'ag.a Laf;': 'jimm/MainMenu.selector Ljimm/util/Selector;',
    'ag.a Lag;': 'jimm/MainMenu._this Ljimm/MainMenu;',
    'af.<init>(II)V': 'jimm/util/Selector.<init>(II)V',
    'cv.f(Le;)V': 'jimm/SplashCanvas.setXStatusToDraw(LDrawControls/Icon;)V',
    'a': 'jimm/Traffic$TrafficScreen',
    'x.a La;': 'jimm/Traffic.trafficScreen Ljimm/Traffic$TrafficScreen;',
    'a.a Lbi;': 'jimm/Traffic$TrafficScreen.trafficTextList LDrawControls/TextList;',
    'a.a Ljavax/microedition/lcdui/Command;': 'jimm/Traffic$TrafficScreen.resetCommand Ljavax/microedition/lcdui/Command;',
    'a.b Ljavax/microedition/lcdui/Command;': 'jimm/Traffic$TrafficScreen.okCommand Ljavax/microedition/lcdui/Command;',
    'm.a(I)V': 'jimm/ContactList.updateTitle(I)V',
    'x.a I': 'jimm/Traffic.allInTraffic I',
    'x.b I': 'jimm/Traffic.allOutTraffic I',
    'x.c I': 'jimm/Traffic.savedCost I',
    'x.d I': 'jimm/Traffic.all_traffic I',
    'x.e I': 'jimm/Traffic.session_traffic I',
    'x.f I': 'jimm/Traffic.sessionInTraffic I',
    'x.g I': 'jimm/Traffic.sessionOutTraffic I',
    'x.h I': 'jimm/Traffic.costPerDaySum I',
    'x.a Ljava/util/Date;': 'jimm/Traffic.savedSince Ljava/util/Date;',
    'x.b Ljava/util/Date;': 'jimm/Traffic.lastTimeUsed Ljava/util/Date;',
    'x.<init>()V': 'jimm/Traffic.<init>()V',
    'x.a()V': 'jimm/Traffic.save()V',
    'x.a(I)Ljava/lang/String;': 'jimm/Traffic.getTrafficString(I)Ljava/lang/String;',
    'x.a()I': 'jimm/Traffic.getSessionTraffic()I',
    'x.a(I)V': 'jimm/Traffic.addInTraffic(I)V',
    'x.b(I)V': 'jimm/Traffic.addOutTraffic(I)V',
    'a.<init>()V': 'jimm/Traffic$TrafficScreen.<init>()V',
    'a.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V': 'jimm/Traffic$TrafficScreen.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V',

    'cl': 'jimm/comm/SendMessageAction',
    'av': 'jimm/comm/PlainMessage',
    'cl.a Lav;': 'jimm/comm/SendMessageAction.plainMsg Ljimm/comm/PlainMessage;',
    'cl.a Lab;': 'jimm/comm/SendMessageAction.fileTrans Ljimm/comm/FileTransferMessage;',
    'cl.a I': 'jimm/comm/SendMessageAction.SEQ1 I',
    'cl.b I': 'jimm/comm/SendMessageAction.msgId1 I',
    'cl.c I': 'jimm/comm/SendMessageAction.msgId2 I',
    'cl.d I': 'jimm/comm/SendMessageAction.msgCounter I',
    'cl.<init>(Lac;)V': 'jimm/comm/SendMessageAction.<init>(Ljimm/comm/Message;)V',
    'cl.a()J': 'jimm/comm/SendMessageAction.getMsgId()J',
    'cl.b()I': 'jimm/comm/SendMessageAction.getMsgCounter()I',
    'cl.a()V': 'jimm/comm/SendMessageAction.init()V',
    'cl.a(Lan;)Z': 'jimm/comm/SendMessageAction.forward(Ljimm/comm/Packet;)Z',
    'cl.a()Z': 'jimm/comm/SendMessageAction.isCompleted()Z',
    'cl.b()Z': 'jimm/comm/SendMessageAction.isError()Z',
    'cl.<clinit>()V': 'jimm/comm/SendMessageAction.<clinit>()V',

    'cc': 'jimm/FileTransfer$ViewFinder',
    'bw': 'jimm/comm/DirectConnectionAction',
    'ab': 'jimm/comm/FileTransferMessage',
    'ao': 'jimm/comm/Icq$PeerConnection',
    'au': 'jimm/comm/DCPacket',
    'cc.a Ljavax/microedition/media/Player;': 'jimm/FileTransfer$ViewFinder.p Ljavax/microedition/media/Player;',
    'cc.a Ljavax/microedition/media/control/VideoControl;': 'jimm/FileTransfer$ViewFinder.vc Ljavax/microedition/media/control/VideoControl;',
    'cc.a Z': 'jimm/FileTransfer$ViewFinder.active Z',
    'cc.b Z': 'jimm/FileTransfer$ViewFinder.viewfinder Z',
    'cc.a Ljavax/microedition/lcdui/Image;': 'jimm/FileTransfer$ViewFinder.img Ljavax/microedition/lcdui/Image;',
    'cc.a [B': 'jimm/FileTransfer$ViewFinder.data [B',
    'cc.a Lp;': 'jimm/FileTransfer$ViewFinder.this$0 Ljimm/FileTransfer;',
    'bw.a I': 'jimm/comm/DirectConnectionAction.state I',
    'bw.a Lab;': 'jimm/comm/DirectConnectionAction.ft Ljimm/comm/FileTransferMessage;',
    'bw.b I': 'jimm/comm/DirectConnectionAction.packets I',
    'bw.a J': 'jimm/comm/DirectConnectionAction.timestamp J',
    'bw.b Z': 'jimm/comm/DirectConnectionAction.cancel Z',
    'ab.a Ljava/lang/String;': 'jimm/comm/FileTransferMessage.filename Ljava/lang/String;',
    'ab.b Ljava/lang/String;': 'jimm/comm/FileTransferMessage.description Ljava/lang/String;',
    'ab.a Ljava/io/InputStream;': 'jimm/comm/FileTransferMessage.fis Ljava/io/InputStream;',
    'ab.a I': 'jimm/comm/FileTransferMessage.fsize I',
    'ao.a Ljavax/microedition/io/SocketConnection;': 'jimm/comm/Icq$PeerConnection.sc Ljavax/microedition/io/SocketConnection;',
    'ao.a Ljava/io/InputStream;': 'jimm/comm/Icq$PeerConnection.is Ljava/io/InputStream;',
    'ao.a Ljava/io/OutputStream;': 'jimm/comm/Icq$PeerConnection.os Ljava/io/OutputStream;',
    'ao.a Z': 'jimm/comm/Icq$PeerConnection.inputCloseFlag Z',
    'ao.a Ljava/lang/Thread;': 'jimm/comm/Icq$PeerConnection.rcvThread Ljava/lang/Thread;',
    'ao.a Ljava/util/Vector;': 'jimm/comm/Icq$PeerConnection.rcvdPackets Ljava/util/Vector;',
    'au.a [B': 'jimm/comm/DCPacket.data [B',
    'cc.<init>(Lp;)V': 'jimm/FileTransfer$ViewFinder.<init>(Ljimm/FileTransfer;)V',
    'cc.b()V': 'jimm/FileTransfer$ViewFinder.reset()V',
    'cc.paint(Ljavax/microedition/lcdui/Graphics;)V': 'jimm/FileTransfer$ViewFinder.paint(Ljavax/microedition/lcdui/Graphics;)V',
    'cc.a(Ljava/lang/String;)V': 'jimm/FileTransfer$ViewFinder.createPlayer(Ljava/lang/String;)V',
    'cc.a()V': 'jimm/FileTransfer$ViewFinder.start()V',
    'cc.a(Ljava/lang/String;)[B': 'jimm/FileTransfer$ViewFinder.getSnapshot(Ljava/lang/String;)[B',
    'cc.c()V': 'jimm/FileTransfer$ViewFinder.takeSnapshot()V',
    'cc.d()V': 'jimm/FileTransfer$ViewFinder.stop()V',
    'cc.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V': 'jimm/FileTransfer$ViewFinder.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V',
    'cc.e()V': 'jimm/FileTransfer$ViewFinder.openSendScreen()V',
    'cc.keyPressed(I)V': 'jimm/FileTransfer$ViewFinder.keyPressed(I)V',
    'bw.<init>(Lab;)V': 'jimm/comm/DirectConnectionAction.<init>(Ljimm/comm/FileTransferMessage;)V',
    'bw.a()V': 'jimm/comm/DirectConnectionAction.init()V',
    'bw.a(Lan;)Z': 'jimm/comm/DirectConnectionAction.forward(Ljimm/comm/Packet;)Z',
    'bw.a()I': 'jimm/comm/DirectConnectionAction.getProgress()I',
    'bw.a()Z': 'jimm/comm/DirectConnectionAction.isCompleted()Z',
    'bw.b()Z': 'jimm/comm/DirectConnectionAction.isError()Z',
    'bw.a(I)V': 'jimm/comm/DirectConnectionAction.onEvent(I)V',
    'ao.<init>()V': 'jimm/comm/Icq$PeerConnection.<init>()V',
    'ao.a(Ljava/lang/String;)V': 'jimm/comm/Icq$PeerConnection.connect(Ljava/lang/String;)V',
    'ao.a()V': 'jimm/comm/Icq$PeerConnection.close()V',
    'ao.a()I': 'jimm/comm/Icq$PeerConnection.available()I',
    'ao.a()Lan;': 'jimm/comm/Icq$PeerConnection.getPacket()Ljimm/comm/Packet;',
    'ao.a(Lan;)V': 'jimm/comm/Icq$PeerConnection.sendPacket(Ljimm/comm/Packet;)V',
    'ao.b()I': 'jimm/comm/Icq$PeerConnection.getLocalPort()I',
    'ao.a()[B': 'jimm/comm/Icq$PeerConnection.getLocalIP()[B',
    'ao.run()V': 'jimm/comm/Icq$PeerConnection.run()V',
    'au.<init>([B)V': 'jimm/comm/DCPacket.<init>([B)V',
    'au.a()[B': 'jimm/comm/DCPacket.toByteArray()[B',
    'ab.<init>(Ljava/lang/String;Lz;Ljava/lang/String;Ljava/lang/String;Ljava/io/InputStream;I)V': 'jimm/comm/FileTransferMessage.<init>(Ljava/lang/String;Ljimm/ContactItem;Ljava/lang/String;Ljava/lang/String;Ljava/io/InputStream;I)V',
    'ab.a(I)[B': 'jimm/comm/FileTransferMessage.getFileSegmentPacket(I)[B',
    'r.a Lao;': 'jimm/comm/Icq.peerC Ljimm/comm/Icq$PeerConnection;',

    'p.a I': 'jimm/FileTransfer.curMode I',
    'p.a Lcc;': 'jimm/FileTransfer.vf Ljimm/FileTransfer$ViewFinder;',
    'p.a Ld;': 'jimm/FileTransfer.name_Desc LDrawControls/VirtualForm;',
    'p.a Ljava/io/InputStream;': 'jimm/FileTransfer.fis Ljava/io/InputStream;',
    'p.b I': 'jimm/FileTransfer.fsize I',
    'p.a Ljava/lang/String;': 'jimm/FileTransfer.exceptionText Ljava/lang/String;',
    'p.a Ljavax/microedition/lcdui/TextField;': 'jimm/FileTransfer.fileNameField Ljavax/microedition/lcdui/TextField;',
    'p.b Ljavax/microedition/lcdui/TextField;': 'jimm/FileTransfer.descriptionField Ljavax/microedition/lcdui/TextField;',
    'p.a Lj;': 'jimm/FileTransfer.webTransfer LDrawControls/FormChoiceGroup;',
    'p.c I': 'jimm/FileTransfer.type I',
    'p.a Lz;': 'jimm/FileTransfer.cItem Ljimm/ContactItem;',
    'u.a Z': 'jimm/FileBrowser.needToSelectDirectory Z',
    'u.b Z': 'jimm/FileBrowser.returnToOptions Z',
    'u.a Lk;': 'jimm/FileBrowser.listener Ljimm/FileBrowserListener;',

    'o.a I': 'jimm/comm/SearchAction.state I',
    'o.a J': 'jimm/comm/SearchAction.lastActivity J',
    'o.a [Ljava/lang/String;': 'jimm/comm/SearchAction.search [Ljava/lang/String;',
    'o.a Lcp;': 'jimm/comm/SearchAction.cont Ljimm/Search;',
    'o.a [I': 'jimm/comm/SearchAction.ages [I',
    'cp.a Ljava/util/Vector;': 'jimm/Search.results Ljava/util/Vector;',
    'cp.a Z': 'jimm/Search.liteVersion Z',
    'cp.a Lcr;': 'jimm/Search.searchForm Ljimm/Search$SearchForm;',

    'as.a I': 'jimm/comm/SaveInfoAction.packetCounter I',
    'as.b I': 'jimm/comm/SaveInfoAction.errorCounter I',
    'as.a Ljava/util/Date;': 'jimm/comm/SaveInfoAction.init Ljava/util/Date;',

    'cf.c Ljava/lang/String;': 'jimm/JimmUI.clipBoardText Ljava/lang/String;',
    'cf.d Ljava/lang/String;': 'jimm/JimmUI.clipBoardHeader Ljava/lang/String;',
    'cf.e Ljava/lang/String;': 'jimm/JimmUI.clipBoardQuotedPrefix Ljava/lang/String;',
    'cf.f Ljava/lang/String;': 'jimm/JimmUI.clipBoardPlainText Ljava/lang/String;',
    'cf.a Z': 'jimm/JimmUI.clipBoardIncoming Z',
    'cf.a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;': 'jimm/JimmUI.insertQuotingChars(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;',

    'co.a I': 'jimm/comm/Util.counter I',
    'ct.a I': 'jimm/comm/UpdateContactListAction.state I',
    'ct.c I': 'jimm/comm/UpdateContactListAction.errorCode I',
    'ct.a J': 'jimm/comm/UpdateContactListAction.lastActivity J',
    'co.a([BIIZ)Ljava/lang/String;': 'jimm/comm/Util.byteArrayToString([BIIZ)Ljava/lang/String;',
    'co.a(Ljava/lang/String;Z)[B': 'jimm/comm/Util.stringToByteArray(Ljava/lang/String;Z)[B',
    'co.a([B)[B': 'jimm/comm/Util.decipherPassword([B)[B',
    'cj.a(II)V': 'jimm/Options.setInt(II)V',
    'cj.a(IJ)V': 'jimm/Options.setLong(IJ)V',
    'cj.a(IZ)V': 'jimm/Options.setBoolean(IZ)V',
    'cj.a(ILjava/lang/String;)V': 'jimm/Options.setString(ILjava/lang/String;)V',
    'cj.a [Ljava/lang/Object;': 'jimm/Options.options [Ljava/lang/Object;',
    'z.a(I)Z': 'jimm/ContactItem.getBooleanValue(I)Z',
    'z.b(I)Z': 'jimm/ContactItem.hasCapability(I)Z',
    'z.b(I)I': 'jimm/ContactItem.getIntValue(I)I',
    'z.c(I)Z': 'jimm/ContactItem.isMessageAvailable(I)Z',
    'cf.a(Ljavax/microedition/lcdui/Command;I)I': 'jimm/JimmUI.getCommandType(Ljavax/microedition/lcdui/Command;I)I',
    'cf.b(Ljava/lang/Object;)V': 'jimm/JimmUI.selectScreen(Ljava/lang/Object;)V',
    'cf.c Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdBack Ljavax/microedition/lcdui/Command;',
    'cf.a I': 'jimm/JimmUI.curScreenTag I',
    'ci.a Ljava/lang/Object;': 'DrawControls/VirtualAlert.previousScreen Ljava/lang/Object;',
    'ag.a(ZZ)V': 'jimm/MainMenu.doExit(ZZ)V',
    'aq.a()V': 'jimm/Templates.clearTemplates()V',
    'cf.e()V': 'jimm/JimmUI.menuRemoveContactSelected()V',
    'cf.f()V': 'jimm/JimmUI.menuRemoveMeSelected()V',
    'r.a I': 'jimm/comm/Icq.flapSEQ I',
    'r.a Lr;': 'jimm/comm/Icq._this Ljimm/comm/Icq;',
    'r.c Z': 'jimm/comm/Icq.disconnected Z',
    'n.b I': 'jimm/comm/ConnectAction.state I',
    'n.b Z': 'jimm/comm/ConnectAction.active Z',
    'n.c Z': 'jimm/comm/ConnectAction.cancel Z',
    'n.a J': 'jimm/comm/ConnectAction.lastActivity J',
    'n.a I': 'jimm/comm/ConnectAction.TIMEOUT I',
    'ap.a Ljava/lang/Object;': 'jimm/comm/Icq$Connection.closeLock Ljava/lang/Object;',
    'ap.a Z': 'jimm/comm/Icq$Connection.inputCloseFlag Z',
    'cv.a Lcv;': 'jimm/SplashCanvas._this Ljimm/SplashCanvas;',
    'cv.a Ljava/lang/String;': 'jimm/SplashCanvas.message Ljava/lang/String;',
    'cv.p I': 'jimm/SplashCanvas.progress I',
    'cv.r I': 'jimm/SplashCanvas.status_index I',
    'cv.c Z': 'jimm/SplashCanvas.isLocked Z',
    'cv.q I': 'jimm/SplashCanvas.availableMessages I',
    'cv.a Le;': 'jimm/SplashCanvas.xstatus_img LDrawControls/Icon;',
    'cv.a Lat;': 'jimm/SplashCanvas.actionTimer Ljimm/TimerTasks;',
    'cv.a Laa;': 'jimm/SplashCanvas.currentAction Ljimm/comm/Action;',
    'cv.c Ljavax/microedition/lcdui/Command;': 'jimm/SplashCanvas.cancelCommnad Ljavax/microedition/lcdui/Command;',
    'cv.r()V': 'jimm/SplashCanvas.cancelActionTimer()V',
    'aa.a(I)V': 'jimm/comm/Action.onEvent(I)V',
    'at.cancel()Z': 'jimm/TimerTasks.cancel()Z',
    'at.a Z': 'jimm/TimerTasks.wasError Z',
    'at.b Z': 'jimm/TimerTasks.canceled Z',
    'at.c I': 'jimm/TimerTasks.type I',
    'cd.e()V': 'DrawControls/VirtualList.repaint()V',
    'cd.a(Ljavax/microedition/lcdui/Command;I)V': 'DrawControls/VirtualList.addCommandEx(Ljavax/microedition/lcdui/Command;I)V',
    'cd.a(Ljavax/microedition/lcdui/Command;)V': 'DrawControls/VirtualList.removeCommandEx(Ljavax/microedition/lcdui/Command;)V',
    'cd.a(Ljavax/microedition/lcdui/CommandListener;)V': 'DrawControls/VirtualList.setCommandListener(Ljavax/microedition/lcdui/CommandListener;)V',
    'cd.l I': 'DrawControls/VirtualList.MENU_RIGHT_BAR I',
    'ap.a(Z)V': 'jimm/comm/Icq$Connection.setInputCloseFlag(Z)V',
    'ap.a()Z': 'jimm/comm/Icq$Connection.getInputCloseFlag()Z',
    'ay.a Ljavax/microedition/io/HttpConnection;': 'jimm/comm/Icq$HTTPConnection.hcm Ljavax/microedition/io/HttpConnection;',
    'ay.b Ljavax/microedition/io/HttpConnection;': 'jimm/comm/Icq$HTTPConnection.hcd Ljavax/microedition/io/HttpConnection;',
    'ay.a Ljava/io/InputStream;': 'jimm/comm/Icq$HTTPConnection.ism Ljava/io/InputStream;',
    'ay.a Ljava/io/OutputStream;': 'jimm/comm/Icq$HTTPConnection.osd Ljava/io/OutputStream;',
    'ay.a Ljava/lang/String;': 'jimm/comm/Icq$HTTPConnection.monitorURL Ljava/lang/String;',
    'ay.b Ljava/lang/String;': 'jimm/comm/Icq$HTTPConnection.sid Ljava/lang/String;',
    'ay.c Ljava/lang/String;': 'jimm/comm/Icq$HTTPConnection.proxy_host Ljava/lang/String;',
    'ay.a I': 'jimm/comm/Icq$HTTPConnection.seq I',
    'ay.b I': 'jimm/comm/Icq$HTTPConnection.proxy_port I',
    'ay.c I': 'jimm/comm/Icq$HTTPConnection.connSeq I',
    'ay.a(Lan;[BII)V': 'jimm/comm/Icq$HTTPConnection.sendPacket(Ljimm/comm/Packet;[BII)V',
    'ay.b()V': 'jimm/comm/Icq$HTTPConnection.stream_close()V',
    'cb.a Ljavax/microedition/io/SocketConnection;': 'jimm/comm/Icq$SOCKSConnection.sc Ljavax/microedition/io/SocketConnection;',
    'cb.a Ljava/io/InputStream;': 'jimm/comm/Icq$SOCKSConnection.is Ljava/io/InputStream;',
    'cb.a Ljava/io/OutputStream;': 'jimm/comm/Icq$SOCKSConnection.os Ljava/io/OutputStream;',
    'cb.a Z': 'jimm/comm/Icq$SOCKSConnection.is_connected Z',
    'cb.b()V': 'jimm/comm/Icq$SOCKSConnection.stream_close()V',
}
# Contact blinking shares one timer in all three May platform variants.
CLASSES['ch'] = 'jimm/ContactItem$1'
SYMBOLS.update({
    'ch': 'jimm/ContactItem$1',
    'cj.a(I)Z': 'jimm/Options.getBoolean(I)Z',
    'cj.a(I)I': 'jimm/Options.getInt(I)I',
    'cj.g I': 'jimm/Options.fontStyle I',
    'cj.b I': 'jimm/Options.blinkColor I',
    'jimm/Jimm.a()Ljava/util/Timer;': 'jimm/Jimm.getTimerRef()Ljava/util/Timer;',
    'ch.<init>(Lz;)V': 'jimm/ContactItem$1.<init>(Ljimm/ContactItem;)V',
    'ch.a Lz;': 'jimm/ContactItem$1.this$0 Ljimm/ContactItem;',
    'z.c()V': 'jimm/ContactItem.prepareToBlink()V',
    'z.b(Z)V': 'jimm/ContactItem.startBlinking(Z)V',
    'z.b()I': 'jimm/ContactItem.getFontStyle()I',
    'z.a()I': 'jimm/ContactItem.getTextColor()I',
    'z.f Z': 'jimm/ContactItem.blinkingOnline Z',
    'z.g Z': 'jimm/ContactItem.blinkingOffline Z',
    'z.h Z': 'jimm/ContactItem.mustStayVisible Z',
    'z.i Z': 'jimm/ContactItem.blinkOnConnect Z',
    'z.r I': 'jimm/ContactItem.blinkingNumber I',
    'z.s I': 'jimm/ContactItem.blinkLimit I',
    'z.a Ljava/util/TimerTask;': 'jimm/ContactItem.BlinkTimer Ljava/util/TimerTask;',
    'm.g()V': 'jimm/ContactList.repaintTree()V',
})

# OptionsForm names come from the inherited source; aliases were checked by descriptor and use.
CLASSES['cg'] = 'jimm/OptionsForm'
CLASSES['ar'] = 'jimm/ColorChooser'
SYMBOLS.update({
    'cg': 'jimm/OptionsForm',
    'cg.a Lbi;': 'jimm/OptionsForm.optionsMenu LDrawControls/TextList;',
    'cg.b Lbi;': 'jimm/OptionsForm.keysMenu LDrawControls/TextList;',
    'cg.c Lbi;': 'jimm/OptionsForm.actionMenu LDrawControls/TextList;',
    'cg.d Lbi;': 'jimm/OptionsForm.colorsMenu LDrawControls/TextList;',
    'cg.e Lbi;': 'jimm/OptionsForm.clientIdMenu LDrawControls/TextList;',
    'cg.a Ld;': 'jimm/OptionsForm.optionsForm LDrawControls/VirtualForm;',
    'cg.b [I': 'jimm/OptionsForm.savedColors [I',
    'cg.b Ljava/lang/String;': 'jimm/OptionsForm.optionsPath Ljava/lang/String;',
    'cg.d Z': 'jimm/OptionsForm.importingOptions Z',
    'cg.e()V': 'jimm/OptionsForm.saveColors()V',
    'cg.f()V': 'jimm/OptionsForm.restoreColors()V',
    'bi.a()I': 'DrawControls/TextList.getSize()I',
    'bi.b()I': 'DrawControls/TextList.getCurrTextIndex()I',
    'bi.b(I)I': 'DrawControls/TextList.getTextIndex(I)I',
    'cj.c I': 'jimm/Options.cursorColor I',
    'cf.d()V': 'jimm/JimmUI.clearAll()V',
    'cf.a Ljavax/microedition/lcdui/TextBox;': 'jimm/JimmUI.messageTextbox Ljavax/microedition/lcdui/TextBox;',
    'cf.a$1385ff()V': 'jimm/JimmUI.setColorScheme()V',
    'ar': 'jimm/ColorChooser',
    'ar.a I': 'jimm/ColorChooser.height I',
    'ar.b I': 'jimm/ColorChooser.width I',
    'ar.c I': 'jimm/ColorChooser._fldif I',
    'ar.d I': 'jimm/ColorChooser._flddo I',
    'ar.e I': 'jimm/ColorChooser.origColor I',
    'ar.f I': 'jimm/ColorChooser._fldbyte I',
    'ar.g I': 'jimm/ColorChooser.optionsColorIndex I',
    'ar.h I': 'jimm/ColorChooser.a I',
    'ar.a Ljava/lang/String;': 'jimm/ColorChooser.s Ljava/lang/String;',
    'ar.b Ljava/lang/String;': 'jimm/ColorChooser.s1 Ljava/lang/String;',
    'ar.a Ljavax/microedition/lcdui/Font;': 'jimm/ColorChooser.font Ljavax/microedition/lcdui/Font;',
})

SYMBOLS.update({
    'at.b I': 'jimm/TimerTasks.oldStatus I',
    'at.a I': 'jimm/TimerTasks.delay I',
    'at.a Ljava/util/Timer;': 'jimm/TimerTasks.timer Ljava/util/Timer;',
    'at.a Laa;': 'jimm/TimerTasks.action Ljimm/comm/Action;',
    'at.a Ljava/lang/Object;': 'jimm/TimerTasks.flashDispl Ljava/lang/Object;',
    'at.a Ljava/lang/String;': 'jimm/TimerTasks.flashText Ljava/lang/String;',
    'at.b Ljava/lang/String;': 'jimm/TimerTasks.flashOldText Ljava/lang/String;',
    'at.d I': 'jimm/TimerTasks.flashCounter I',
    'at.a J': 'jimm/TimerTasks.currData J',
    'at.a()V': 'jimm/TimerTasks.setStatusTimer()V',
    'at.a(I)V': 'jimm/TimerTasks.statusChange(I)V',
    'at.a()Z': 'jimm/TimerTasks.isCanceled()Z',
    'at.a()I': 'jimm/TimerTasks.getType()I',
    'at.b()Z': 'jimm/TimerTasks.checkFlashControlIsActive()Z',
    'at.b()V': 'jimm/TimerTasks.flashRestoreOldCaption()V',
    'cv.n()V': 'jimm/SplashCanvas.lockScreen()V',
    'cv.a(I)V': 'jimm/SplashCanvas.setStatusToDraw(I)V',
    'cf.a(J)I': 'jimm/JimmUI.getStatusImageIndex(J)I',
    'r.a(I)V': 'jimm/comm/Icq.setOnlineStatus(I)V',
    'ag.a Z': 'jimm/MainMenu.haveToRestoreStatus Z',
})

# Main-menu members read directly from the May class, independent of decompiler aliases.
SYMBOLS.update({
    'ag.a Lag;': 'jimm/MainMenu._this Ljimm/MainMenu;',
    'ag.a Ljavax/microedition/lcdui/Command;': 'jimm/MainMenu.sendCommand Ljavax/microedition/lcdui/Command;',
    'ag.b Ljavax/microedition/lcdui/Command;': 'jimm/MainMenu.exitCommand Ljavax/microedition/lcdui/Command;',
    'ag.a Lbi;': 'jimm/MainMenu.statusList LDrawControls/TextList;',
    'ag.b Lbi;': 'jimm/MainMenu.privateStatusActList LDrawControls/TextList;',
    'ag.c Lbi;': 'jimm/MainMenu.groupActList LDrawControls/TextList;',
    'ag.d Lbi;': 'jimm/MainMenu.list LDrawControls/TextList;',
    'ag.a I': 'jimm/MainMenu.status I',
    'ag.a Laf;': 'jimm/MainMenu.selector Ljimm/util/Selector;',
    'ag.a [I': 'jimm/MainMenu.groupIds [I',
    'ag.a Ld;': 'jimm/MainMenu.textBoxForm LDrawControls/VirtualForm;',
    'ag.a Ljavax/microedition/lcdui/TextField;': 'jimm/MainMenu.uinTextField Ljavax/microedition/lcdui/TextField;',
    'ag.a Ljavax/microedition/lcdui/TextBox;': 'jimm/MainMenu.statusMessage Ljavax/microedition/lcdui/TextBox;',
    'ag.a()Le;': 'jimm/MainMenu.getXStatusImage()LDrawControls/Icon;',
    'ag.b()Le;': 'jimm/MainMenu.getPrivateStatusImage()LDrawControls/Icon;',
    'ag.a()V': 'jimm/MainMenu.build()V',
    'ag.b()V': 'jimm/MainMenu.activate()V',
    'ag.c()V': 'jimm/MainMenu.initStatusList()V',
    'ag.d()V': 'jimm/MainMenu.showXStatusSelector()V',
    'ag.e()V': 'jimm/MainMenu.showPrivateStatus()V',
    'ag.a(I)V': 'jimm/MainMenu.setOnlineStatus(I)V',
    'ag.a(I)I': 'jimm/MainMenu.statusMsgIdxSelector(I)I',
    'ag.b(I)V': 'jimm/MainMenu.CLManagementItemSelected(I)V',
    'ag.f()V': 'jimm/MainMenu.actionMMCLAct()V',
})

SYMBOLS.update({
    'jimm/Jimm.a Ljavax/microedition/lcdui/Display;': 'jimm/Jimm.display Ljavax/microedition/lcdui/Display;',
    'ai.a(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/ResourceBundle.getString(Ljava/lang/String;)Ljava/lang/String;',
    'bj.a(I)Le;': 'jimm/comm/XStatus.getStatusImage(I)LDrawControls/Icon;',
    'm.e Lf;': 'jimm/ContactList.psIcons LDrawControls/ImageList;',
    'm.b Lf;': 'jimm/ContactList.menuIcons LDrawControls/ImageList;',
    'f.a(I)Le;': 'DrawControls/ImageList.elementAt(I)LDrawControls/Icon;',
    'cd.b(Ljavax/microedition/lcdui/Display;)V': 'DrawControls/VirtualList.activate(Ljavax/microedition/lcdui/Display;)V',
    'cd.b(I)V': 'DrawControls/VirtualList.setFontSize(I)V',
    'cf.a(Ljava/lang/Object;)V': 'jimm/JimmUI.setLastScreen(Ljava/lang/Object;)V',
    'r.c()Z': 'jimm/comm/Icq.isConnected()Z',
    'm.a()V': 'jimm/ContactList.activate()V',
    'd.a()V': 'DrawControls/VirtualForm.clear()V',
    'd.a(Ljavax/microedition/lcdui/Item;)V': 'DrawControls/VirtualForm.append(Ljavax/microedition/lcdui/Item;)V',
})

CLASSES.update({'am': 'DrawControls/VirtualCanvas', 'bx': 'DrawControls/VirtualListCommands'})
# Caption icon aliases were checked against ContactList.activate and actual touch routes.
SYMBOLS.update({
    'cd.a Lam;': 'DrawControls/VirtualList.virtualCanvas LDrawControls/VirtualCanvas;',
    'cd.a Ljava/lang/String;': 'DrawControls/VirtualList.caption Ljava/lang/String;',
    'cd.d Ljavax/microedition/lcdui/Font;': 'DrawControls/VirtualList.capFont Ljavax/microedition/lcdui/Font;',
    'cd.a Le;': 'DrawControls/VirtualList.capImage LDrawControls/Icon;',
    'cd.b Le;': 'DrawControls/VirtualList.capXstImage LDrawControls/Icon;',
    'cd.c Le;': 'DrawControls/VirtualList.capPrivateImage LDrawControls/Icon;',
    'cd.d Le;': 'DrawControls/VirtualList.capHappyImage LDrawControls/Icon;',
    'cd.e Le;': 'DrawControls/VirtualList.capSoundImage LDrawControls/Icon;',
    'cd.z I': 'DrawControls/VirtualList.captionStatusEnd I',
    'cd.A I': 'DrawControls/VirtualList.captionXStatusEnd I',
    'cd.B I': 'DrawControls/VirtualList.captionPrivateEnd I',
    'cd.C I': 'DrawControls/VirtualList.captionSoundStart I',
    'cd.h I': 'DrawControls/VirtualList.lastPointerYCrd I',
    'cd.i I': 'DrawControls/VirtualList.lastPointerTopItem I',
    'cd.b I': 'DrawControls/VirtualList.currItem I',
    'cd.d I': 'DrawControls/VirtualList.topItem I',
    'cd.c I': 'DrawControls/VirtualList.borderWidth I',
    'cd.e I': 'DrawControls/VirtualList.bkgrndColor I',
    'cd.g I': 'DrawControls/VirtualList.capBkCOlor I',
    'cd.b Ljavax/microedition/lcdui/Image;': 'DrawControls/VirtualList.bDIimage Ljavax/microedition/lcdui/Image;',
    'cd.a Ljavax/microedition/lcdui/Image;': 'DrawControls/VirtualList.bgimage Ljavax/microedition/lcdui/Image;',
    'cd.a Lcn;': 'DrawControls/VirtualList.paintedItem LDrawControls/ListItem;',
    'cd.a Lbx;': 'DrawControls/VirtualList.vlCommands LDrawControls/VirtualListCommands;',
    'cd.m()I': 'DrawControls/VirtualList.getCapHeight()I',
    'cd.n()I': 'DrawControls/VirtualList.getHeightInternal()I',
    'cd.j()I': 'DrawControls/VirtualList.getWidthInternal()I',
    'cd.o()I': 'DrawControls/VirtualList.getMenuBarHeight()I',
    'cd.g()I': 'DrawControls/VirtualList.getNonScrollerArea()I',
    'cd.e()I': 'DrawControls/VirtualList.getVisCount()I',
    'cd.a()I': 'DrawControls/VirtualList.getSize()I',
    'cd.a(I)I': 'DrawControls/VirtualList.getItemHeight(I)I',
    'cd.i()I': 'DrawControls/VirtualList.getFontHeight()I',
    'cd.b()I': 'DrawControls/VirtualList.getDrawHeight()I',
    'cd.a(I)Z': 'DrawControls/VirtualList.isItemSelected(I)Z',
    'cd.i()V': 'DrawControls/VirtualList.storelastItemIndexes()V',
    'cd.j()V': 'DrawControls/VirtualList.repaintIfLastIndexesChanged()V',
    'cd.g()V': 'DrawControls/VirtualList.invalidate()V',
    'cd.a()Z': 'DrawControls/VirtualList.itemSelected()Z',
    'cd.b()Z': 'DrawControls/VirtualList.isActive()Z',
    'cd.a(IIII)Z': 'DrawControls/VirtualList.pointerPressedOnUtem(IIII)Z',
    'cd.a(II)I': 'DrawControls/VirtualList.transformColorLight(II)I',
    'cd.d(I)I': 'DrawControls/VirtualList.getInverseColor(I)I',
    'cd.a(Ljavax/microedition/lcdui/Graphics;III)V': 'DrawControls/VirtualList.paintAllOnGraphics(Ljavax/microedition/lcdui/Graphics;III)V',
    'e.a()I': 'DrawControls/Icon.getWidth()I',
    'e.b()I': 'DrawControls/Icon.getHeight()I',
    'cn.a()V': 'DrawControls/ListItem.clear()V',
    'bx.a(Lcd;)V': 'DrawControls/VirtualListCommands.vlCursorMoved(LDrawControls/VirtualList;)V',
    'cj.d I': 'jimm/Options.cursorAlpha I',
    'cj.e I': 'jimm/Options.captionAlpha I',
    'cj.f I': 'jimm/Options.softbarAlpha I',
    'm.a Law;': 'jimm/ContactList.tree LDrawControls/VirtualTree;',
    'm.b Z': 'jimm/ContactList.enterContactMenu Z',
})

CLASSES.update({'bb': 'jimm/GroupItem', 'bj': 'jimm/comm/XStatus', 'bs': 'jimm/ContactListItem', 'f': 'DrawControls/ImageList', 'jimm/Jimm': 'jimm/Jimm'})
SYMBOLS.update({
    'bb.a I': 'jimm/GroupItem.id I',
    'bb.a Ljava/lang/String;': 'jimm/GroupItem.name Ljava/lang/String;',
    'bb.a Z': 'jimm/GroupItem.expanded Z',
    'bb.b I': 'jimm/GroupItem.onlineCount I',
    'bb.c I': 'jimm/GroupItem.totalCount I',
    'bb.a Lbj;': 'jimm/GroupItem.xstatus Ljimm/comm/XStatus;',
    'cd.e Z': 'DrawControls/VirtualList.xStatusOnRight Z',
    'cd.q I': 'DrawControls/VirtualList.leftOffset I',
    'cd.r I': 'DrawControls/VirtualList.rightOffset I',
    'cd.s I': 'DrawControls/VirtualList.curMenuItemIndex I',
    'cd.t I': 'DrawControls/VirtualList.fontView I',
    'cd.x I': 'DrawControls/VirtualList.visibleItemsMenuCount I',
    'cd.y I': 'DrawControls/VirtualList.topMenuItem I',
    'cd.f I': 'DrawControls/VirtualList.textColor I',
    'cd.v I': 'DrawControls/VirtualList.capTxtColor I',
    'cd.d Z': 'DrawControls/VirtualList.fullScreen Z',
    'cd.n I': 'DrawControls/VirtualList.uiState I',
    'cd.e Ljavax/microedition/lcdui/Font;': 'DrawControls/VirtualList.menuBarFont Ljavax/microedition/lcdui/Font;',
    'cd.f Ljavax/microedition/lcdui/Font;': 'DrawControls/VirtualList.menuItemsFont Ljavax/microedition/lcdui/Font;',
    'cn.a I': 'DrawControls/ListItem.fontStyle I',
    'cn.b I': 'DrawControls/ListItem.color I',
    'cn.c I': 'DrawControls/ListItem.horizOffset I',
    'cn.a Ljava/lang/String;': 'DrawControls/ListItem.text Ljava/lang/String;',
    'cn.a Le;': 'DrawControls/ListItem.image LDrawControls/Icon;',
    'cn.b Le;': 'DrawControls/ListItem.xStatusImg LDrawControls/Icon;',
    'cn.c Le;': 'DrawControls/ListItem.happyImg LDrawControls/Icon;',
    'cn.d Le;': 'DrawControls/ListItem.bDayImg LDrawControls/Icon;',
    'cn.e Le;': 'DrawControls/ListItem.authImg LDrawControls/Icon;',
    'cn.f Le;': 'DrawControls/ListItem.ignoreImg LDrawControls/Icon;',
    'cn.g Le;': 'DrawControls/ListItem.visibilityImg LDrawControls/Icon;',
    'cn.h Le;': 'DrawControls/ListItem.clientImg LDrawControls/Icon;',
    'ax.a Z': 'DrawControls/TreeNode.expanded Z',
    'ax.a Ljava/lang/Object;': 'DrawControls/TreeNode.data Ljava/lang/Object;',
    'ax.a(Z)V': 'DrawControls/TreeNode.setExpanded(Z)V',
    'ax.a()Z': 'DrawControls/TreeNode.getExpanded()Z',
    'm.a Ljava/util/Hashtable;': 'jimm/ContactList.gNodes Ljava/util/Hashtable;',
    'm.a(Lbb;)Lax;': 'jimm/ContactList.addGroupNodeInternal(Ljimm/GroupItem;)LDrawControls/TreeNode;',
})

SYMBOLS.update({
    'm.a Ljava/util/Vector;': 'jimm/ContactList.cItems Ljava/util/Vector;',
    'm.b Ljava/util/Vector;': 'jimm/ContactList.gItems Ljava/util/Vector;',
    'm.a Lm;': 'jimm/ContactList._this Ljimm/ContactList;',
    'm.a Z': 'jimm/ContactList.justConnected Z',
    'm.d Z': 'jimm/ContactList.haveToBeCleared Z',
    'm.e Z': 'jimm/ContactList.treeBuilt Z',
    'm.a I': 'jimm/ContactList.ssiListLastChangeTime I',
    'm.b I': 'jimm/ContactList.ssiNumberOfItems I',
    'm.c I': 'jimm/ContactList.onlineCounter I',
    'm.d I': 'jimm/ContactList.sortType I',
    'm.e I': 'jimm/ContactList.lastUnknownStatus I',
    'bs.a()Ljava/lang/String;': 'jimm/ContactListItem.getSortText()Ljava/lang/String;',
    'bs.a(I)I': 'jimm/ContactListItem.getSortWeight(I)I',
    'm.a(Lax;Lax;)I': 'jimm/ContactList.vtCompareNodes(LDrawControls/TreeNode;LDrawControls/TreeNode;)I',
    'm.i()V': 'jimm/ContactList.sortAll()V',
    'm.j()V': 'jimm/ContactList.buildTree()V',
    'm.a(Lax;Lbb;)V': 'jimm/ContactList.calcGroupData(LDrawControls/TreeNode;Ljimm/GroupItem;)V',
    'm.a(I)[Lz;': 'jimm/ContactList.getGroupItems(I)[Ljimm/ContactItem;',
    'm.a(I)Lbb;': 'jimm/ContactList.getGroupById(I)Ljimm/GroupItem;',
    'm.a(Ljava/lang/String;)Lz;': 'jimm/ContactList.getItembyUIN(Ljava/lang/String;)Ljimm/ContactItem;',
    'm.a(I)Lz;': 'jimm/ContactList.getCItem(I)Ljimm/ContactItem;',
    'm.c()I': 'jimm/ContactList.getSize()I',
    'm.d()V': 'jimm/ContactList.setStatusesOffline()V',
    'm.b()V': 'jimm/ContactList.save()V',
    'm.f()V': 'jimm/ContactList.safeSave()V',
    'm.a(Lz;ZZ)V': 'jimm/ContactList.contactChanged(Ljimm/ContactItem;ZZ)V',
    'm.a(Lz;ZZI)V': 'jimm/ContactList.statusChanged(Ljimm/ContactItem;ZZI)V',
    'm.a(Lz;)V': 'jimm/ContactList.removeContactItem(Ljimm/ContactItem;)V',
    'm.b(Lz;)V': 'jimm/ContactList.addContactItem(Ljimm/ContactItem;)V',
    'm.a(Ljava/lang/String;II[B[BIIIIIIII)V': 'jimm/ContactList.update(Ljava/lang/String;II[B[BIIIIIIII)V',
    'z.a Z': 'jimm/ContactItem.readXtraz Z',
    'z.b Z': 'jimm/ContactItem.autoAnswered Z',
    'z.a J': 'jimm/ContactItem.lastOfflineActivity J',
    'z.b J': 'jimm/ContactItem.statusUpdateTime J',
    'z.e Ljava/lang/String;': 'jimm/ContactItem.offlineTime Ljava/lang/String;',
    'z.a(I)Ljava/lang/String;': 'jimm/ContactItem.getStringValue(I)Ljava/lang/String;',
    'z.a(IZ)V': 'jimm/ContactItem.setBooleanValue(IZ)V',
    'z.a(II)V': 'jimm/ContactItem.setIntValue(II)V',
    'bb.a(II)V': 'jimm/GroupItem.setCounters(II)V',
    'bb.b(II)V': 'jimm/GroupItem.updateCounters(II)V',
    'aw.a Lax;': 'DrawControls/VirtualTree.root LDrawControls/TreeNode;',
    'aw.b()V': 'DrawControls/VirtualTree.clear()V',
    'aw.b(Lax;)V': 'DrawControls/VirtualTree.sortNode(LDrawControls/TreeNode;)V',
    'aw.a(Lax;Ljava/lang/Object;)Lax;': 'DrawControls/VirtualTree.addNode(LDrawControls/TreeNode;Ljava/lang/Object;)LDrawControls/TreeNode;',
    'aw.b(Lax;Lax;)I': 'DrawControls/VirtualTree.getIndexOfChild(LDrawControls/TreeNode;LDrawControls/TreeNode;)I',
    'aw.a(Lax;I)V': 'DrawControls/VirtualTree.deleteChild(LDrawControls/TreeNode;I)V',
})

CLASSES['az'] = 'DrawControls/VirtualTreeCommands'
SYMBOLS.update({
    'aw.a Ljava/util/Vector;': 'DrawControls/VirtualTree.drawItems Ljava/util/Vector;',
    'aw.b Z': 'DrawControls/VirtualTree.isChanged Z',
    'aw.o I': 'DrawControls/VirtualTree.stepSize I',
    'aw.c Z': 'DrawControls/VirtualTree.autoExpand Z',
    'aw.a Laz;': 'DrawControls/VirtualTree.commands LDrawControls/VirtualTreeCommands;',
    'aw.b Lax;': 'DrawControls/VirtualTree.lastNode LDrawControls/TreeNode;',
    'ax.a Ljava/util/Vector;': 'DrawControls/TreeNode.items Ljava/util/Vector;',
    'ax.a I': 'DrawControls/TreeNode.level I',
    'az.a(Lax;Lax;)I': 'DrawControls/VirtualTreeCommands.vtCompareNodes(LDrawControls/TreeNode;LDrawControls/TreeNode;)I',
    'az.a(Lax;Lcn;)V': 'DrawControls/VirtualTreeCommands.vtGetItemDrawData(LDrawControls/TreeNode;LDrawControls/ListItem;)V',
    'cd.k()V': 'DrawControls/VirtualList.lock()V',
    'cd.l()V': 'DrawControls/VirtualList.unlock()V',
    'cd.c()V': 'DrawControls/VirtualList.afterUnlock()V',
})

SYMBOLS['aw.a(I)Lax;'] = 'DrawControls/VirtualTree.getDrawItem(I)LDrawControls/TreeNode;'
SYMBOLS['aw.a()V'] = 'DrawControls/VirtualTree.checkToRebuildTree()V'
SYMBOLS['aw.a(I)V'] = 'DrawControls/VirtualTree.setStepSize(I)V'
SYMBOLS['aw.a()Lax;'] = 'DrawControls/VirtualTree.getCurrentItem()LDrawControls/TreeNode;'
SYMBOLS['aw.a(Lax;)V'] = 'DrawControls/VirtualTree.setCurrentItem(LDrawControls/TreeNode;)V'
SYMBOLS['aw.a(Ljava/util/Vector;Lax;Lax;)Z'] = 'DrawControls/VirtualTree.buildNodePath(Ljava/util/Vector;LDrawControls/TreeNode;LDrawControls/TreeNode;)Z'
SYMBOLS['aw.a()Z'] = 'DrawControls/VirtualTree.itemSelected()Z'
SYMBOLS['aw.a(IIII)Z'] = 'DrawControls/VirtualTree.pointerPressedOnUtem(IIII)Z'
SYMBOLS['aw.a()I'] = 'DrawControls/VirtualTree.getSize()I'
SYMBOLS['aw.n()V'] = 'DrawControls/VirtualTree.rebuildTreeIntItems()V'
SYMBOLS['aw.b(Lax;I)V'] = 'DrawControls/VirtualTree.fillTreeIntItems(LDrawControls/TreeNode;I)V'
SYMBOLS['aw.a(ILcn;)V'] = 'DrawControls/VirtualTree.get(ILDrawControls/ListItem;)V'
SYMBOLS['aw.a(Ljavax/microedition/lcdui/Graphics;IIIIII)V'] = 'DrawControls/VirtualTree.drawItemData(Ljavax/microedition/lcdui/Graphics;IIIIII)V'
SYMBOLS['aw.a(Lax;Ljava/lang/Object;)Lax;'] = 'DrawControls/VirtualTree.addNode(LDrawControls/TreeNode;Ljava/lang/Object;)LDrawControls/TreeNode;'
SYMBOLS['aw.a(Lax;Lax;)Lax;'] = 'DrawControls/VirtualTree.findParent(LDrawControls/TreeNode;LDrawControls/TreeNode;)LDrawControls/TreeNode;'
SYMBOLS['aw.a(Lax;)Z'] = 'DrawControls/VirtualTree.removeNode(LDrawControls/TreeNode;)Z'
SYMBOLS['aw.b(Lax;)V'] = 'DrawControls/VirtualTree.sortNode(LDrawControls/TreeNode;)V'
SYMBOLS['aw.a(Lax;Lax;I)V'] = 'DrawControls/VirtualTree.insertChild(LDrawControls/TreeNode;LDrawControls/TreeNode;I)V'
SYMBOLS['aw.a(Lax;I)V'] = 'DrawControls/VirtualTree.deleteChild(LDrawControls/TreeNode;I)V'
SYMBOLS['aw.b(Lax;Lax;)I'] = 'DrawControls/VirtualTree.getIndexOfChild(LDrawControls/TreeNode;LDrawControls/TreeNode;)I'
SYMBOLS['aw.a(Lax;Z)V'] = 'DrawControls/VirtualTree.setExpandFlag(LDrawControls/TreeNode;Z)V'
SYMBOLS['aw.b()V'] = 'DrawControls/VirtualTree.clear()V'
SYMBOLS['aw.o()V'] = 'DrawControls/VirtualTree.storeLastNode()V'
SYMBOLS['aw.c()V'] = 'DrawControls/VirtualTree.afterUnlock()V'
SYMBOLS['aw.p()V'] = 'DrawControls/VirtualTree.restoreLastNode()V'
SYMBOLS['ax.a()V'] = 'DrawControls/TreeNode.clear()V'
SYMBOLS['ax.a()Z'] = 'DrawControls/TreeNode.getExpanded()Z'
SYMBOLS['ax.a()I'] = 'DrawControls/TreeNode.size()I'
SYMBOLS['ax.a(I)Lax;'] = 'DrawControls/TreeNode.elementAt(I)LDrawControls/TreeNode;'
SYMBOLS['ax.a(Lax;)Lax;'] = 'DrawControls/TreeNode.addItem(LDrawControls/TreeNode;)LDrawControls/TreeNode;'
SYMBOLS['ax.a(Lax;I)V'] = 'DrawControls/TreeNode.insertChild(LDrawControls/TreeNode;I)V'
SYMBOLS['ax.a(I)V'] = 'DrawControls/TreeNode.removeItem(I)V'
SYMBOLS['ax.a(Lax;)I'] = 'DrawControls/TreeNode.findItem(LDrawControls/TreeNode;)I'
SYMBOLS['ax.a(Ljava/util/Vector;Lax;Laz;)I'] = 'DrawControls/TreeNode.getInsertionPos(Ljava/util/Vector;LDrawControls/TreeNode;LDrawControls/VirtualTreeCommands;)I'
SYMBOLS['ax.a(Laz;)V'] = 'DrawControls/TreeNode.sort(LDrawControls/VirtualTreeCommands;)V'


CLASSES.update({'c': 'DrawControls/FormIcon', 'i': 'DrawControls/FormItem'})
SYMBOLS.update({
    'd.<init>(Ljava/lang/String;)V': 'DrawControls/VirtualForm.<init>(Ljava/lang/String;)V',
    'd.a(Ljavax/microedition/lcdui/Item;)V': 'DrawControls/VirtualForm.append(Ljavax/microedition/lcdui/Item;)V',
    'd.a(Lax;Ljavax/microedition/lcdui/Item;)V': 'DrawControls/VirtualForm.appendChoices(LDrawControls/TreeNode;Ljavax/microedition/lcdui/Item;)V',
    'd.a(Lax;Lcn;)V': 'DrawControls/VirtualForm.vtGetItemDrawData(LDrawControls/TreeNode;LDrawControls/ListItem;)V',
    'd.a(IIII)Z': 'DrawControls/VirtualForm.pointerPressedOnUtem(IIII)Z',
    'd.a(Lax;Lax;)I': 'DrawControls/VirtualForm.vtCompareNodes(LDrawControls/TreeNode;LDrawControls/TreeNode;)I',
    'd.a(Lcd;II)V': 'DrawControls/VirtualForm.vlKeyPress(LDrawControls/VirtualList;II)V',
    'd.a(Lcd;)V': 'DrawControls/VirtualForm.vlCursorMoved(LDrawControls/VirtualList;)V',
    'd.b(Lcd;)V': 'DrawControls/VirtualForm.vlItemClicked(LDrawControls/VirtualList;)V',
    'd.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V': 'DrawControls/VirtualForm.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V',
    'd.a(Lj;I)Le;': 'DrawControls/VirtualForm.choiceImage(LDrawControls/FormChoiceGroup;I)LDrawControls/Icon;',
    'd.a(Ljavax/microedition/lcdui/TextField;)Ljava/lang/String;': 'DrawControls/VirtualForm.fieldText(Ljavax/microedition/lcdui/TextField;)Ljava/lang/String;',
    'd.a(Ljavax/microedition/lcdui/TextField;Ljavax/microedition/lcdui/Font;)Ljava/lang/String;': 'DrawControls/VirtualForm.fitField(Ljavax/microedition/lcdui/TextField;Ljavax/microedition/lcdui/Font;)Ljava/lang/String;',
    'd.a(Ljava/lang/String;)Ljava/lang/String;': 'DrawControls/VirtualForm.nonNull(Ljava/lang/String;)Ljava/lang/String;',
    'd.a(Ljava/lang/Object;)Ljava/lang/String;': 'DrawControls/VirtualForm.getItemText(Ljava/lang/Object;)Ljava/lang/String;',
    'd.a()V': 'DrawControls/VirtualForm.clear()V',
    'c.<init>(IIIIZ)V': 'DrawControls/FormIcon.<init>(IIIIZ)V',
    'c.a(Ljavax/microedition/lcdui/Graphics;II)V': 'DrawControls/FormIcon.drawByLeft(Ljavax/microedition/lcdui/Graphics;II)V',
    'c.a(I)V': 'DrawControls/FormIcon.setValue(I)V',
    'i.<init>(Ljavax/microedition/lcdui/Item;Ljava/lang/String;ILe;)V': 'DrawControls/FormItem.<init>(Ljavax/microedition/lcdui/Item;Ljava/lang/String;ILDrawControls/Icon;)V',
    'j.<init>(Ljava/lang/String;I)V': 'DrawControls/FormChoiceGroup.<init>(Ljava/lang/String;I)V',
    'j.<init>(Ljava/lang/String;[Ljava/lang/String;)V': 'DrawControls/FormChoiceGroup.<init>(Ljava/lang/String;[Ljava/lang/String;)V',
    'cn.<init>()V': 'DrawControls/ListItem.<init>()V',
    'cn.a()V': 'DrawControls/ListItem.clear()V',
    'd.a Lf;': 'DrawControls/VirtualForm.formImages LDrawControls/ImageList;',
    'd.a Ljavax/microedition/lcdui/ItemStateListener;': 'DrawControls/VirtualForm.itemStateListener Ljavax/microedition/lcdui/ItemStateListener;',
    'd.b Z': 'DrawControls/VirtualForm.firstItem Z',
    'd.c Ljavax/microedition/lcdui/Command;': 'DrawControls/VirtualForm.editOk Ljavax/microedition/lcdui/Command;',
    'd.d Ljavax/microedition/lcdui/Command;': 'DrawControls/VirtualForm.editCancel Ljavax/microedition/lcdui/Command;',
    'd.a Li;': 'DrawControls/VirtualForm.currentDrawItem LDrawControls/FormItem;',
    'i.a Ljavax/microedition/lcdui/Item;': 'DrawControls/FormItem.item Ljavax/microedition/lcdui/Item;',
    'i.a Le;': 'DrawControls/FormItem.image LDrawControls/Icon;',
    'i.a I': 'DrawControls/FormItem.choiceIndex I',
    'i.a Ljava/lang/String;': 'DrawControls/FormItem.text Ljava/lang/String;',
    'j.a I': 'DrawControls/FormChoiceGroup.choiceType I',
    'c.a I': 'DrawControls/FormIcon.value I',
    'c.b I': 'DrawControls/FormIcon.stepWidth I',
    'c.e I': 'DrawControls/FormIcon.maximum I',
    'c.a Z': 'DrawControls/FormIcon.gauge Z',
    'e.c I': 'DrawControls/Icon.width I',
    'e.d I': 'DrawControls/Icon.height I',
    'cd.d()I': 'DrawControls/VirtualList.getTextColor()I',
    'cd.e(I)V': 'DrawControls/VirtualList.moveCursor(I)V',
    'cd.k(I)V': 'DrawControls/VirtualList.setCurrentItem(I)V',
    'cd.c(I)I': 'DrawControls/VirtualList.getGameAction(I)I',
})


CLASSES.update({'bc': 'DrawControls/TextItem', 'bm': 'DrawControls/TextLine'})
SYMBOLS.update({
    'bi.<init>(Ljava/lang/String;)V': 'DrawControls/TextList.<init>(Ljava/lang/String;)V',
    'bi.a()I': 'DrawControls/TextList.getSize()I',
    'bi.a(I)Lbm;': 'DrawControls/TextList.getLine(I)LDrawControls/TextLine;',
    'bi.a(I)Z': 'DrawControls/TextList.isItemSelected(I)Z',
    'bi.a(ILcn;)V': 'DrawControls/TextList.get(ILDrawControls/ListItem;)V',
    'bi.a()V': 'DrawControls/TextList.clear()V',
    'bi.a(Ljava/lang/String;IIIZC)V': 'DrawControls/TextList.internAdd(Ljava/lang/String;IIIZC)V',
    'bi.a(I)I': 'DrawControls/TextList.getItemHeight(I)I',
    'bi.a(Ljavax/microedition/lcdui/Graphics;IIIIII)V': 'DrawControls/TextList.drawItemData(Ljavax/microedition/lcdui/Graphics;IIIIII)V',
    'bi.a(IZ)V': 'DrawControls/TextList.moveCursor(IZ)V',
    'bi.a(IZI)Ljava/lang/String;': 'DrawControls/TextList.getTextByIndex(IZI)Ljava/lang/String;',
    'bi.a(I)V': 'DrawControls/TextList.selectTextByIndex(I)V',
    'bi.a(IZ)Ljava/lang/String;': 'DrawControls/TextList.getCurrText(IZ)Ljava/lang/String;',
    'bi.b(I)I': 'DrawControls/TextList.getTextIndex(I)I',
    'bi.b()I': 'DrawControls/TextList.getCurrTextIndex()I',
    'bi.a(IIII)V': 'DrawControls/TextList.setColors(IIII)V',
    'bi.a(I)Lbi;': 'DrawControls/TextList.doCRLF(I)LDrawControls/TextList;',
    'bi.a(Le;Ljava/lang/String;I)Lbi;': 'DrawControls/TextList.addImage(LDrawControls/Icon;Ljava/lang/String;I)LDrawControls/TextList;',
    'bi.m()I': 'DrawControls/TextList.getTextAreaWidth()I',
    'bi.a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;': 'DrawControls/TextList.replace(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;',
    'bi.a(Ljava/lang/String;IIII)V': 'DrawControls/TextList.addBigTextInternal(Ljava/lang/String;IIII)V',
    'bi.a(Ljava/lang/String;III)Lbi;': 'DrawControls/TextList.addBigText(Ljava/lang/String;III)LDrawControls/TextList;',
    'bc.<init>()V': 'DrawControls/TextItem.<init>()V',
    'bc.a(I)I': 'DrawControls/TextItem.getHeight(I)I',
    'bc.b(I)I': 'DrawControls/TextItem.getWidth(I)I',
    'bm.<init>()V': 'DrawControls/TextLine.<init>()V',
    'bm.a(I)Lbc;': 'DrawControls/TextLine.elementAt(I)LDrawControls/TextItem;',
    'bm.a(Lbc;)V': 'DrawControls/TextLine.add(LDrawControls/TextItem;)V',
    'bm.a(I)I': 'DrawControls/TextLine.getHeight(I)I',
    'bm.b(I)I': 'DrawControls/TextLine.getWidth(I)I',
    'bm.a(IILjavax/microedition/lcdui/Graphics;ILcd;)V': 'DrawControls/TextLine.paint(IILjavax/microedition/lcdui/Graphics;ILDrawControls/VirtualList;)V',
    'bm.a(Ljava/lang/StringBuffer;)V': 'DrawControls/TextLine.readText(Ljava/lang/StringBuffer;)V',
    'bi.a Ljava/util/Vector;': 'DrawControls/TextList.lines Ljava/util/Vector;',
    'bi.a Ljava/util/Hashtable;': 'DrawControls/TextList.msgTable Ljava/util/Hashtable;',
    'bc.a Le;': 'DrawControls/TextItem.image LDrawControls/Icon;',
    'bc.a Ljava/lang/String;': 'DrawControls/TextItem.text Ljava/lang/String;',
    'bc.a I': 'DrawControls/TextItem.fontAndColor I',
    'bc.b I': 'DrawControls/TextItem.itemHeigthAndWidth I',
    'bm.a Ljava/util/Vector;': 'DrawControls/TextLine.items Ljava/util/Vector;',
    'bm.b I': 'DrawControls/TextLine.height I',
    'bm.a I': 'DrawControls/TextLine.bigTextIndex I',
    'bm.a C': 'DrawControls/TextLine.last_charaster C',
    'cd.c()I': 'DrawControls/VirtualList.getFontSize()I',
    'cd.f()I': 'DrawControls/VirtualList.getCursorMode()I',
    'cd.h()I': 'DrawControls/VirtualList.getCurrIndex()I',
    'cd.d(I)V': 'DrawControls/VirtualList.checkCurrItem(I)V',
    'cd.h()V': 'DrawControls/VirtualList.checkTopItem()V',
    'cd.b(I)Z': 'DrawControls/VirtualList.visibleItem(I)Z',
    'cd.a(IZ)V': 'DrawControls/VirtualList.moveCursor(IZ)V',
    'cd.a(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;IIII)V': 'DrawControls/VirtualList.drawString(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;IIII)V',
    'e.b(Ljavax/microedition/lcdui/Graphics;II)V': 'DrawControls/Icon.drawImage(Ljavax/microedition/lcdui/Graphics;II)V',
})

SYMBOLS.update({
    'ci.<init>(Ljava/lang/Object;Ljava/lang/String;I)V': 'DrawControls/VirtualAlert.<init>(Ljava/lang/Object;Ljava/lang/String;I)V',
    'ci.d(Z)V': 'DrawControls/VirtualAlert.updateText(Z)V',
    'ci.a(Ljavax/microedition/lcdui/Graphics;)V': 'DrawControls/VirtualAlert.paint(Ljavax/microedition/lcdui/Graphics;)V',
    'ci.a(II)V': 'DrawControls/VirtualAlert.doKeyreaction(II)V',
    'ci.i(I)V': 'DrawControls/VirtualAlert.pointerDragged(I)V',
    'ci.a(ILcn;)V': 'DrawControls/VirtualAlert.get(ILDrawControls/ListItem;)V',
    'ci.a()I': 'DrawControls/VirtualAlert.getSize()I',
    'ci.a Ljava/lang/String;': 'DrawControls/VirtualAlert.text Ljava/lang/String;',
    'ci.a Lbi;': 'DrawControls/VirtualAlert.lines LDrawControls/TextList;',
    'ci.a Ljavax/microedition/lcdui/Font;': 'DrawControls/VirtualAlert.font Ljavax/microedition/lcdui/Font;',
    'ci.o I': 'DrawControls/VirtualAlert.popupWidth I',
    'ci.p I': 'DrawControls/VirtualAlert.popupHeight I',
    'ci.q I': 'DrawControls/VirtualAlert.visibleLines I',
    'ci.r I': 'DrawControls/VirtualAlert.fontSize I',
    'cd.a(Ljavax/microedition/lcdui/Graphics;)V': 'DrawControls/VirtualList.paint(Ljavax/microedition/lcdui/Graphics;)V',
    'cd.a(II)V': 'DrawControls/VirtualList.doKeyreaction(II)V',
    'cd.a(Ljavax/microedition/lcdui/Graphics;IIII)V': 'DrawControls/VirtualList.drawScroller(Ljavax/microedition/lcdui/Graphics;IIII)V',
    'cf.a(Lcd;Z)V': 'jimm/JimmUI.setColorScheme(LDrawControls/VirtualList;Z)V',
})

SYMBOLS.update({
    'aq.<init>()V': 'jimm/Templates.<init>()V',
    'aq.a(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V': 'jimm/Templates.selectTemplate(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V',
    'aq.a(Lcd;II)V': 'jimm/Templates.vlKeyPress(LDrawControls/VirtualList;II)V',
    'aq.a(Lcd;)V': 'jimm/Templates.vlCursorMoved(LDrawControls/VirtualList;)V',
    'aq.b(Lcd;)V': 'jimm/Templates.vlItemClicked(LDrawControls/VirtualList;)V',
    'aq.b()V': 'jimm/Templates.select()V',
    'aq.c()V': 'jimm/Templates.refreshList()V',
    'aq.d()V': 'jimm/Templates.load()V',
    'aq.e()V': 'jimm/Templates.save()V',
    'aq.a()Ljava/lang/String;': 'jimm/Templates.getTemlate()Ljava/lang/String;',
    'aq.f()V': 'jimm/Templates.addContextCommand()V',
    'aq.g()V': 'jimm/Templates.removeContextCommand()V',
    'aq.h()V': 'jimm/Templates.refresh()V',
    'aq.a Ljavax/microedition/lcdui/Command;': 'jimm/Templates.selectTemplateCommand Ljavax/microedition/lcdui/Command;',
    'aq.b Ljavax/microedition/lcdui/Command;': 'jimm/Templates.backCommand Ljavax/microedition/lcdui/Command;',
    'aq.c Ljavax/microedition/lcdui/Command;': 'jimm/Templates.newTemplateCommand Ljavax/microedition/lcdui/Command;',
    'aq.d Ljavax/microedition/lcdui/Command;': 'jimm/Templates.editTemplateCommand Ljavax/microedition/lcdui/Command;',
    'aq.e Ljavax/microedition/lcdui/Command;': 'jimm/Templates.deleteCurrentTemplateCommand Ljavax/microedition/lcdui/Command;',
    'aq.f Ljavax/microedition/lcdui/Command;': 'jimm/Templates.clearCommand Ljavax/microedition/lcdui/Command;',
    'aq.g Ljavax/microedition/lcdui/Command;': 'jimm/Templates.addCommand Ljavax/microedition/lcdui/Command;',
    'aq.h Ljavax/microedition/lcdui/Command;': 'jimm/Templates.editCommand Ljavax/microedition/lcdui/Command;',
    'aq.i Ljavax/microedition/lcdui/Command;': 'jimm/Templates.cancelCommand Ljavax/microedition/lcdui/Command;',
    'aq.a Lbi;': 'jimm/Templates.templateList LDrawControls/TextList;',
    'aq.a Laq;': 'jimm/Templates._this Ljimm/Templates;',
    'aq.a Ljavax/microedition/lcdui/TextBox;': 'jimm/Templates.templateTextbox Ljavax/microedition/lcdui/TextBox;',
    'aq.b Ljavax/microedition/lcdui/TextBox;': 'jimm/Templates.textBox Ljavax/microedition/lcdui/TextBox;',
    'aq.a Ljava/util/Vector;': 'jimm/Templates.templates Ljava/util/Vector;',
    'aq.a Ljava/lang/Object;': 'jimm/Templates.lastScreen Ljava/lang/Object;',
    'aq.a I': 'jimm/Templates.caretPos I',
    'cd.c Ljavax/microedition/lcdui/Command;': 'DrawControls/VirtualList.defaultCommand Ljavax/microedition/lcdui/Command;',
})

SYMBOLS.update({
    'af.o I': 'jimm/util/Selector.cols I',
    'af.p I': 'jimm/util/Selector.rows I',
    'af.q I': 'jimm/util/Selector.imgHeight I',
    'af.r I': 'jimm/util/Selector.itemHeight I',
    'af.s I': 'jimm/util/Selector.increment I',
    'af.t I': 'jimm/util/Selector.curCol I',
    'af.u I': 'jimm/util/Selector.imagesCount I',
    'af.v I': 'jimm/util/Selector.selectorType I',
    'af.a Laf;': 'jimm/util/Selector._this Ljimm/util/Selector;',
    'af.a [I': 'jimm/util/Selector.colorTable [I',
    'af.a(IIII)Z': 'jimm/util/Selector.pointerPressedOnUtem(IIII)Z',
    'af.a(Ljavax/microedition/lcdui/Graphics;IIIIII)V': 'jimm/util/Selector.drawItemData(Ljavax/microedition/lcdui/Graphics;IIIIII)V',
    'af.a()V': 'jimm/util/Selector.showCurrName()V',
    'af.a(I)I': 'jimm/util/Selector.getItemHeight(I)I',
    'af.a()I': 'jimm/util/Selector.getSize()I',
    'af.m()I': 'jimm/util/Selector.getLength()I',
    'af.b()I': 'jimm/util/Selector.getCurrSelectedIdx()I',
    'af.b(I)I': 'jimm/util/Selector.getColorIndex(I)I',
    'af.a(ILcn;)V': 'jimm/util/Selector.get(ILDrawControls/ListItem;)V',
    'af.a(Lcd;II)V': 'jimm/util/Selector.vlKeyPress(LDrawControls/VirtualList;II)V',
    'af.a(Lcd;)V': 'jimm/util/Selector.vlCursorMoved(LDrawControls/VirtualList;)V',
    'af.b(Lcd;)V': 'jimm/util/Selector.vlItemClicked(LDrawControls/VirtualList;)V',
    'bo.a Lf;': 'jimm/Emotions.images LDrawControls/ImageList;',
    'bj.a()Lf;': 'jimm/comm/XStatus.getXStatusImageList()LDrawControls/ImageList;',
    'bj.a()I': 'jimm/comm/XStatus.getXStatusCount()I',
    'bj.a(I)Ljava/lang/String;': 'jimm/comm/XStatus.getStatusAsString(I)Ljava/lang/String;',
})

CLASSES['b'] = 'jimm/JSR75FileSystem'
SYMBOLS.update({
    'u.b()V': 'jimm/FileBrowser.reset()V',
    'u.a(Ljava/lang/String;)I': 'jimm/FileBrowser.getNodeWeight(Ljava/lang/String;)I',
    'u.a(Lax;Lax;)I': 'jimm/FileBrowser.vtCompareNodes(LDrawControls/TreeNode;LDrawControls/TreeNode;)I',
    'u.c()V': 'jimm/FileBrowser.rebuildTree()V',
    'u.a(ZZ)V': 'jimm/FileBrowser.setParameters(ZZ)V',
    'u.a(Lk;)V': 'jimm/FileBrowser.setListener(Ljimm/FileBrowserListener;)V',
    'u.a(Ljava/lang/String;)V': 'jimm/FileBrowser.updateTreeCaptionAndCommands(Ljava/lang/String;)V',
    'u.a(Lax;Lcn;)V': 'jimm/FileBrowser.vtGetItemDrawData(LDrawControls/TreeNode;LDrawControls/ListItem;)V',
    'u.a(Lcd;)V': 'jimm/FileBrowser.vlCursorMoved(LDrawControls/VirtualList;)V',
    'u.b(Lcd;)V': 'jimm/FileBrowser.vlItemClicked(LDrawControls/VirtualList;)V',
    'u.a(Lcd;II)V': 'jimm/FileBrowser.vlKeyPress(LDrawControls/VirtualList;II)V',
    'u.a()V': 'jimm/FileBrowser.activate()V',
    'u.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V': 'jimm/FileBrowser.commandAction(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V',
    'b.a(Ljava/lang/String;Z)[Ljava/lang/String;': 'jimm/JSR75FileSystem.getDirectoryContents(Ljava/lang/String;Z)[Ljava/lang/String;',
    'b.a(Ljava/lang/String;)V': 'jimm/JSR75FileSystem.openFile(Ljava/lang/String;)V',
    'b.a()Ljava/io/OutputStream;': 'jimm/JSR75FileSystem.openOutputStream()Ljava/io/OutputStream;',
    'b.a()Ljava/io/InputStream;': 'jimm/JSR75FileSystem.openInputStream()Ljava/io/InputStream;',
    'b.a()V': 'jimm/JSR75FileSystem.close()V',
    'b.a()J': 'jimm/JSR75FileSystem.fileSize()J',
    'b.a()Ljava/lang/String;': 'jimm/JSR75FileSystem.getName()Ljava/lang/String;',
    'u.a Lu;': 'jimm/FileBrowser._this Ljimm/FileBrowser;',
    'u.a Law;': 'jimm/FileBrowser.tree LDrawControls/VirtualTree;',
    'u.a Lf;': 'jimm/FileBrowser.imageList LDrawControls/ImageList;',
    'u.a [Ljava/lang/String;': 'jimm/FileBrowser.items [Ljava/lang/String;',
    'u.a Ljava/lang/String;': 'jimm/FileBrowser.currDir Ljava/lang/String;',
    'u.a Ljavax/microedition/lcdui/Command;': 'jimm/FileBrowser.openCommand Ljavax/microedition/lcdui/Command;',
    'b.a Ljavax/microedition/io/file/FileConnection;': 'jimm/JSR75FileSystem.fileConnection Ljavax/microedition/io/file/FileConnection;',
    'k.a(Ljava/lang/String;)V': 'jimm/FileBrowserListener.onFileSelect(Ljava/lang/String;)V',
    'k.b(Ljava/lang/String;)V': 'jimm/FileBrowserListener.onDirectorySelect(Ljava/lang/String;)V',
    'k.a()Lz;': 'jimm/FileBrowserListener.getCItem()Ljimm/ContactItem;',
    'w.a(Ljava/lang/String;Z)[Ljava/lang/String;': 'jimm/FileSystem.getDirectoryContents(Ljava/lang/String;Z)[Ljava/lang/String;',
})

CLASSES.update({'ai': 'jimm/util/ResourceBundle', 'cm': 'DrawControls/AniImageList', 'aj': 'DrawControls/LightControl'})
SYMBOLS.update({
    'ai.a [Ljava/lang/String;': 'jimm/util/ResourceBundle.langAvailable [Ljava/lang/String;',
    'ai.a Ljava/lang/String;': 'jimm/util/ResourceBundle.currUiLanguage Ljava/lang/String;',
    'ai.a Ljava/util/Hashtable;': 'jimm/util/ResourceBundle.resources Ljava/util/Hashtable;',
    'ai.a(Ljava/lang/String;)V': 'jimm/util/ResourceBundle.setCurrUiLanguage(Ljava/lang/String;)V',
    'ai.a()V': 'jimm/util/ResourceBundle.loadLang()V',
    'ai.a$7a1ba7c4(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/ResourceBundle.getString$7a1ba7c4(Ljava/lang/String;)Ljava/lang/String;',
    'bo.a Lbo;': 'jimm/Emotions._this Ljimm/Emotions;',
    'bo.a Laf;': 'jimm/Emotions.selector Ljimm/util/Selector;',
    'f.a()I': 'DrawControls/ImageList.size()I',
    'f.a(Ljava/lang/String;II)V': 'DrawControls/ImageList.load(Ljava/lang/String;II)V',
    'aj.a(Z)V': 'DrawControls/LightControl.flash(Z)V',
})


# Verified packet fields and surviving signatures; parser behavior is exercised before optimization.
CLASSES.update({'an': 'jimm/comm/Packet', 'ak': 'jimm/comm/SnacPacket', 'h': 'jimm/comm/ConnectPacket', 'bn': 'jimm/comm/DisconnectPacket', 'bu': 'jimm/comm/ToIcqSrvPacket', 'ck': 'jimm/comm/FromIcqSrvPacket'})
SYMBOLS.update({
    'an.c I': 'jimm/comm/Packet.sequence I',
    'an.a I': 'jimm/comm/Packet.flapChannel I',
    'an.a [B': 'jimm/comm/Packet.flapData [B',
    'an.a(I)V': 'jimm/comm/Packet.setSequence(I)V',
    'an.<init>()V': 'jimm/comm/Packet.<init>()V',
    'an.<init>([B)V': 'jimm/comm/Packet.<init>([B)V',
    'an.a()[B': 'jimm/comm/Packet.toByteArray()[B',
    'an.a([BII)Lan;': 'jimm/comm/Packet.parse([BII)Ljimm/comm/Packet;',
    'ak.a I': 'jimm/comm/SnacPacket.family I',
    'ak.b I': 'jimm/comm/SnacPacket.command I',
    'ak.d I': 'jimm/comm/SnacPacket.snacFlags I',
    'ak.a J': 'jimm/comm/SnacPacket.reference J',
    'ak.a [B': 'jimm/comm/SnacPacket.extData [B',
    'ak.b [B': 'jimm/comm/SnacPacket.data [B',
    'ak.<init>(IIIIJ[B[B)V': 'jimm/comm/SnacPacket.<init>(IIIIJ[B[B)V',
    'ak.<init>(IIJ[B[B)V': 'jimm/comm/SnacPacket.<init>(IIJ[B[B)V',
    'ak.a()I': 'jimm/comm/SnacPacket.getFamily()I',
    'ak.b()I': 'jimm/comm/SnacPacket.getCommand()I',
    'ak.b()[B': 'jimm/comm/SnacPacket.getData()[B',
    'ak.a()[B': 'jimm/comm/SnacPacket.toByteArray()[B',
    'ak.a([BII)Lan;': 'jimm/comm/SnacPacket.parse([BII)Ljimm/comm/Packet;',
    'h.a [B': 'jimm/comm/ConnectPacket.cookie [B',
    'h.a Ljava/lang/String;': 'jimm/comm/ConnectPacket.uin Ljava/lang/String;',
    'h.b Ljava/lang/String;': 'jimm/comm/ConnectPacket.password Ljava/lang/String;',
    'h.<init>(I)V': 'jimm/comm/ConnectPacket.<init>(I)V',
    'h.<init>()V': 'jimm/comm/ConnectPacket.<init>()V',
    'h.<init>(I[B)V': 'jimm/comm/ConnectPacket.<init>(I[B)V',
    'h.<init>([B)V': 'jimm/comm/ConnectPacket.<init>([B)V',
    'h.<init>(ILjava/lang/String;Ljava/lang/String;)V': 'jimm/comm/ConnectPacket.<init>(ILjava/lang/String;Ljava/lang/String;)V',
    'h.<init>(Ljava/lang/String;Ljava/lang/String;)V': 'jimm/comm/ConnectPacket.<init>(Ljava/lang/String;Ljava/lang/String;)V',
    'h.a()I': 'jimm/comm/ConnectPacket.getType()I',
    'h.a()[B': 'jimm/comm/ConnectPacket.toByteArray()[B',
    'h.a([BII)Lan;': 'jimm/comm/ConnectPacket.parse([BII)Ljimm/comm/Packet;',
    'bn.a Ljava/lang/String;': 'jimm/comm/DisconnectPacket.uin Ljava/lang/String;',
    'bn.b Ljava/lang/String;': 'jimm/comm/DisconnectPacket.server Ljava/lang/String;',
    'bn.a [B': 'jimm/comm/DisconnectPacket.cookie [B',
    'bn.a I': 'jimm/comm/DisconnectPacket.error I',
    'bn.c Ljava/lang/String;': 'jimm/comm/DisconnectPacket.description Ljava/lang/String;',
    'bn.<init>(ILjava/lang/String;Ljava/lang/String;[B)V': 'jimm/comm/DisconnectPacket.<init>(ILjava/lang/String;Ljava/lang/String;[B)V',
    'bn.<init>(IILjava/lang/String;)V': 'jimm/comm/DisconnectPacket.<init>(IILjava/lang/String;)V',
    'bn.<init>(I)V': 'jimm/comm/DisconnectPacket.<init>(I)V',
    'bn.<init>()V': 'jimm/comm/DisconnectPacket.<init>()V',
    'bn.a()I': 'jimm/comm/DisconnectPacket.getType()I',
    'bn.b()I': 'jimm/comm/DisconnectPacket.getError()I',
    'bn.a()[B': 'jimm/comm/DisconnectPacket.toByteArray()[B',
    'bn.a([BII)Lan;': 'jimm/comm/DisconnectPacket.parse([BII)Ljimm/comm/Packet;',
    'bu.d I': 'jimm/comm/ToIcqSrvPacket.icqSequence I',
    'bu.a Ljava/lang/String;': 'jimm/comm/ToIcqSrvPacket.uin Ljava/lang/String;',
    'bu.e I': 'jimm/comm/ToIcqSrvPacket.subcommand I',
    'bu.<init>(IJIILjava/lang/String;I[B[B)V': 'jimm/comm/ToIcqSrvPacket.<init>(IJIILjava/lang/String;I[B[B)V',
    'bu.<init>(Ljava/lang/String;I[B[B)V': 'jimm/comm/ToIcqSrvPacket.<init>(Ljava/lang/String;I[B[B)V',
    'bu.a()[B': 'jimm/comm/ToIcqSrvPacket.toByteArray()[B',
    'bu.a([BII)Lan;': 'jimm/comm/ToIcqSrvPacket.parse([BII)Ljimm/comm/Packet;',
    'ck.d I': 'jimm/comm/FromIcqSrvPacket.icqSequence I',
    'ck.a Ljava/lang/String;': 'jimm/comm/FromIcqSrvPacket.uin Ljava/lang/String;',
    'ck.e I': 'jimm/comm/FromIcqSrvPacket.subcommand I',
    'ck.<init>(IJIILjava/lang/String;I[B[B)V': 'jimm/comm/FromIcqSrvPacket.<init>(IJIILjava/lang/String;I[B[B)V',
    'ck.a()[B': 'jimm/comm/FromIcqSrvPacket.toByteArray()[B',
    'ck.a([BII)Lan;': 'jimm/comm/FromIcqSrvPacket.parse([BII)Ljimm/comm/Packet;',
})


# SSI lists, removal and authorization actions, verified against real action execution.
CLASSES.update({'cq': 'jimm/comm/ServerListsAction', 'al': 'jimm/comm/RemoveMeAction', 'v': 'jimm/comm/SysNoticeAction', 's': 'jimm/comm/SystemNotice'})
SYMBOLS.update({
    'cq.a I': 'jimm/comm/ServerListsAction.subaction I',
    'cq.b I': 'jimm/comm/ServerListsAction.list I',
    'cq.a Lz;': 'jimm/comm/ServerListsAction.item Ljimm/ContactItem;',
    'cq.a Ljava/util/Date;': 'jimm/comm/ServerListsAction.init Ljava/util/Date;',
    'cq.c I': 'jimm/comm/ServerListsAction.id I',
    'cq.d I': 'jimm/comm/ServerListsAction.packetCounter I',
    'cq.<init>(ILz;)V': 'jimm/comm/ServerListsAction.<init>(ILjimm/ContactItem;)V',
    'cq.a()V': 'jimm/comm/ServerListsAction.init()V',
    'cq.a(Lan;)Z': 'jimm/comm/ServerListsAction.forward(Ljimm/comm/Packet;)Z',
    'cq.a()Z': 'jimm/comm/ServerListsAction.isCompleted()Z',
    'cq.b()Z': 'jimm/comm/ServerListsAction.isError()Z',
    'cq.a()I': 'jimm/comm/ServerListsAction.getProgress()I',
    'al.a Ljava/lang/String;': 'jimm/comm/RemoveMeAction.uin Ljava/lang/String;',
    'al.<init>(Ljava/lang/String;)V': 'jimm/comm/RemoveMeAction.<init>(Ljava/lang/String;)V',
    'al.a()V': 'jimm/comm/RemoveMeAction.init()V',
    'al.a(Lan;)Z': 'jimm/comm/RemoveMeAction.forward(Ljimm/comm/Packet;)Z',
    'al.a()Z': 'jimm/comm/RemoveMeAction.isCompleted()Z',
    'al.b()Z': 'jimm/comm/RemoveMeAction.isError()Z',
    'v.a Ls;': 'jimm/comm/SysNoticeAction.notice Ljimm/comm/SystemNotice;',
    'v.<init>(Ls;)V': 'jimm/comm/SysNoticeAction.<init>(Ljimm/comm/SystemNotice;)V',
    'v.a()V': 'jimm/comm/SysNoticeAction.init()V',
    'v.a(Lan;)Z': 'jimm/comm/SysNoticeAction.forward(Ljimm/comm/Packet;)Z',
    'v.a()Z': 'jimm/comm/SysNoticeAction.isCompleted()Z',
    'v.b()Z': 'jimm/comm/SysNoticeAction.isError()Z',
    's.a I': 'jimm/comm/SystemNotice.sysnotetype I',
    's.a Z': 'jimm/comm/SystemNotice.AUTH_granted Z',
    's.a Ljava/lang/String;': 'jimm/comm/SystemNotice.reason Ljava/lang/String;',
    's.<init>(ILjava/lang/String;ZLjava/lang/String;)V': 'jimm/comm/SystemNotice.<init>(ILjava/lang/String;ZLjava/lang/String;)V',
    'z.m()I': 'jimm/ContactItem.getVisibleId()I',
    'z.n()I': 'jimm/ContactItem.getInvisibleId()I',
    'z.l()I': 'jimm/ContactItem.getIgnoreId()I',
    'z.e(I)V': 'jimm/ContactItem.setVisibleId(I)V',
    'z.f(I)V': 'jimm/ContactItem.setInvisibleId(I)V',
    'z.d(I)V': 'jimm/ContactItem.setIgnoreId(I)V',
    'z.c()I': 'jimm/ContactItem.getUIN()I',
    'co.a(Ljava/io/ByteArrayOutputStream;IZ)V': 'jimm/comm/Util.writeWord(Ljava/io/ByteArrayOutputStream;IZ)V',
    'co.a(Ljava/io/ByteArrayOutputStream;Ljava/lang/String;Z)V': 'jimm/comm/Util.writeLenAndString(Ljava/io/ByteArrayOutputStream;Ljava/lang/String;Z)V',
    'co.a(Ljava/lang/String;Z)[B': 'jimm/comm/Util.stringToByteArray(Ljava/lang/String;Z)[B',
    'co.a$1385f3()J': 'jimm/comm/Util.createCurrentDate$1385f3()J',
    'ac.c Ljava/lang/String;': 'jimm/comm/Message.sndrUin Ljava/lang/String;',
    'ac.a Ljava/lang/String;': 'jimm/comm/Message.rcvrUin Ljava/lang/String;',
    'ac.a J': 'jimm/comm/Message.newDate J',
})


# Verified real RunnableImpl dispatcher, typed payload helpers and callback identities.
CLASSES.update({'l': 'jimm/RunnableImpl'})
SYMBOLS.update({
    'l.<init>(I[Ljava/lang/Object;)V': 'jimm/RunnableImpl.<init>(I[Ljava/lang/Object;)V',
    'l.run()V': 'jimm/RunnableImpl.run()V',
    'l.a(I[Ljava/lang/Object;)V': 'jimm/RunnableImpl.callSerially(I[Ljava/lang/Object;)V',
    'l.a(ILjava/lang/Object;)V': 'jimm/RunnableImpl.callSerially(ILjava/lang/Object;)V',
    'l.a(Lac;)V': 'jimm/RunnableImpl.addMessageSerially(Ljimm/comm/Message;)V',
    'l.a(Ljava/lang/String;Z)V': 'jimm/RunnableImpl.BeginTyping(Ljava/lang/String;Z)V',
    'l.a$4e9ee315([Ljava/lang/Object;)Z': 'jimm/RunnableImpl.getBoolean$4e9ee315([Ljava/lang/Object;)Z',
    'l.a([Ljava/lang/Object;II)V': 'jimm/RunnableImpl.setInt([Ljava/lang/Object;II)V',
    'l.a([Ljava/lang/Object;I)I': 'jimm/RunnableImpl.getInt([Ljava/lang/Object;I)I',
    'l.a I': 'jimm/RunnableImpl.type I',
    'l.a [Ljava/lang/Object;': 'jimm/RunnableImpl.data [Ljava/lang/Object;',
    'l.a Ljavax/microedition/midlet/MIDlet;': 'jimm/RunnableImpl.midlet Ljavax/microedition/midlet/MIDlet;',
    'm.a(Ljava/lang/String;Z)V': 'jimm/ContactList.BeginTyping(Ljava/lang/String;Z)V',
    'm.a(Lac;Z)V': 'jimm/ContactList.addMessage(Ljimm/comm/Message;Z)V',
    'm.a$505cff1c(Ljava/lang/String;)V': 'jimm/ContactList.update$505cff1c(Ljava/lang/String;)V',
    'm.a(I)V': 'jimm/ContactList.updateTitle(I)V',
    'm.a(Ljava/lang/String;II[B[BIIIIIIII)V': 'jimm/ContactList.update(Ljava/lang/String;II[B[BIIIIIIII)V',
    'm.b(Lz;)V': 'jimm/ContactList.addContactItem(Ljimm/ContactItem;)V',
    'm.d()V': 'jimm/ContactList.setStatusesOffline()V',
    'm.b(I)V': 'jimm/ContactList.playSoundNotification(I)V',
    'm.c()V': 'jimm/ContactList.beforeConnect()V',
    'cf.a([Ljava/lang/String;)V': 'jimm/JimmUI.showUserInfo([Ljava/lang/String;)V',
    'cf.a()V': 'jimm/JimmUI.backToLastScreen()V',
    'ag.a(Ljava/lang/String;)V': 'jimm/MainMenu.activate(Ljava/lang/String;)V',
    'r.a()V': 'jimm/comm/Icq.nextSrvHost()V',
    'r.b()V': 'jimm/comm/Icq.connect()V',
    'jimm/Jimm.a(Z)V': 'jimm/Jimm.setMinimized(Z)V',
    'x.a()I': 'jimm/Traffic.getSessionTraffic()I',
    'cj.a(I)I': 'jimm/Options.getInt(I)I',
    'cj.a(I)Z': 'jimm/Options.getBoolean(I)Z',
    'jimm/Jimm.b Z': 'jimm/Jimm.is_phone_SE Z',
    'r.b I': 'jimm/comm/Icq.reconnect_attempts I',
    'r.a()Z': 'jimm/comm/Icq.isDisconnected()Z',
    'ae.a(Lac;)Z': 'jimm/comm/ActionListener.isSpam(Ljimm/comm/Message;)Z',
})

# ProGuard merged these static Xtraz helpers into Icon in the rebuilt JAR.
# This records their observed optimizer destination, without changing source ownership.
SOURCE_OWNERS = {'ba': 'DrawControls/Icon'}
CLASSES.update({'ba': 'jimm/comm/XtrazSM'})
SYMBOLS.update({
    'ba.a(Ljava/lang/String;IJJLjava/lang/String;)[B': 'DrawControls/Icon.a(Ljava/lang/String;IJJLjava/lang/String;)[B',
    'ba.a([BI)I': 'DrawControls/Icon.a([BI)I',
    'ba.b([BI)I': 'DrawControls/Icon.b([BI)I',
    'co.c(Ljava/lang/String;)Ljava/lang/String;': 'jimm/comm/Util.DeMangleXml(Ljava/lang/String;)Ljava/lang/String;',
    'co.d(Ljava/lang/String;)Ljava/lang/String;': 'jimm/comm/Util.MangleXml(Ljava/lang/String;)Ljava/lang/String;',
})

# Verified converter fields, retained helper signatures and real UTF-8 detector.
CLASSES.update({'bp': 'jimm/util/StringConvertor'})
SYMBOLS.update({
    'bp.c(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/StringConvertor.loadFromResource(Ljava/lang/String;)Ljava/lang/String;',
    'bp.d(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/StringConvertor.removeCr(Ljava/lang/String;)Ljava/lang/String;',
    'bp.e(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/StringConvertor.toUpperCase(Ljava/lang/String;)Ljava/lang/String;',
    'bp.a(C)C': 'jimm/util/StringConvertor.toLowerCase(C)C',
    'bp.f(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/StringConvertor.convertChar(Ljava/lang/String;)Ljava/lang/String;',
    'bp.a(Ljava/util/Vector;)[Ljava/lang/String;': 'jimm/util/StringConvertor.vectorToArray(Ljava/util/Vector;)[Ljava/lang/String;',
    'bp.a(Ljava/lang/String;Ljava/util/Vector;)V': 'jimm/util/StringConvertor.convertorParser(Ljava/lang/String;Ljava/util/Vector;)V',
    'bp.a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/StringConvertor.convert(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;',
    'bp.a(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/StringConvertor.detransliterate(Ljava/lang/String;)Ljava/lang/String;',
    'bp.b(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/StringConvertor.transliterate(Ljava/lang/String;)Ljava/lang/String;',
    'bp.a Ljava/lang/String;': 'jimm/util/StringConvertor.name Ljava/lang/String;',
    'bp.a [Ljava/lang/String;': 'jimm/util/StringConvertor.from [Ljava/lang/String;',
    'bp.b [Ljava/lang/String;': 'jimm/util/StringConvertor.to [Ljava/lang/String;',
    'bp.a I': 'jimm/util/StringConvertor.maxWordLength I',
    'bp.a [Lbp;': 'jimm/util/StringConvertor.converters [Ljimm/util/StringConvertor;',
    'co.a([BII)Z': 'jimm/comm/Util.isDataUTF8([BII)Z',
})

# Verified birthday worker/RMS methods and their real calendar helpers.
CLASSES.update({'cs': 'jimm/util/NoticeOnBirthDay'})
SYMBOLS.update({
    'cs.a()V': 'jimm/util/NoticeOnBirthDay.refreshBday()V',
    'cs.run()V': 'jimm/util/NoticeOnBirthDay.run()V',
    'cs.a(Ljava/lang/String;II)V': 'jimm/util/NoticeOnBirthDay.additemB(Ljava/lang/String;II)V',
    'cs.a(Ljava/lang/String;)V': 'jimm/util/NoticeOnBirthDay.deleteBitem(Ljava/lang/String;)V',
    'cs.a(Ljava/lang/String;)I': 'jimm/util/NoticeOnBirthDay.checkDatacurrData(Ljava/lang/String;)I',
    'cs.b()V': 'jimm/util/NoticeOnBirthDay.load()V',
    'cs.c()V': 'jimm/util/NoticeOnBirthDay.save()V',
    'cs.a Lcs;': 'jimm/util/NoticeOnBirthDay._this Ljimm/util/NoticeOnBirthDay;',
    'cs.a Ljava/util/Vector;': 'jimm/util/NoticeOnBirthDay.a0 Ljava/util/Vector;',
    'cs.b Ljava/util/Vector;': 'jimm/util/NoticeOnBirthDay.a1 Ljava/util/Vector;',
    'cs.c Ljava/util/Vector;': 'jimm/util/NoticeOnBirthDay.a2 Ljava/util/Vector;',
    'cs.a J': 'jimm/util/NoticeOnBirthDay.bData1 J',
    'co.a(ZZ)J': 'jimm/comm/Util.createCurrentDate(ZZ)J',
    'co.a(J)[I': 'jimm/comm/Util.createDate(J)[I',
    'co.a(IIIIII)J': 'jimm/comm/Util.createLongTime(IIIIII)J',
    'co.b(I)I': 'jimm/comm/Util.convertDateMonToSimpleMon(I)I',
    'co.a(J)J': 'jimm/comm/Util.gmtTimeToLocalTime(J)J',
    'co.ab [B': 'jimm/comm/Util.dayCounts [B',
    'co.a [I': 'jimm/comm/Util.monthIndexes [I',
    'z.b()Ljava/lang/String;': 'jimm/ContactItem.getUinString()Ljava/lang/String;',
})

# Verified socket members. The close body is confirmed in all three May platforms.
CLASSES.update({'bf': 'jimm/comm/Icq$SOCKETConnection'})
SYMBOLS.update({
    'bf.a Ljavax/microedition/io/SocketConnection;': 'jimm/comm/Icq$SOCKETConnection.sc Ljavax/microedition/io/SocketConnection;',
    'bf.a Ljava/io/InputStream;': 'jimm/comm/Icq$SOCKETConnection.is Ljava/io/InputStream;',
    'bf.a Ljava/io/OutputStream;': 'jimm/comm/Icq$SOCKETConnection.os Ljava/io/OutputStream;',
    'bf.a I': 'jimm/comm/Icq$SOCKETConnection.nextIcqSequence I',
    'bf.a(Ljava/lang/String;)V': 'jimm/comm/Icq$SOCKETConnection.connect(Ljava/lang/String;)V',
    'bf.a(Lan;)V': 'jimm/comm/Icq$SOCKETConnection.sendPacket(Ljimm/comm/Packet;)V',
    'bf.a()V': 'jimm/comm/Icq$SOCKETConnection.close()V',
    'bf.b()I': 'jimm/comm/Icq$SOCKETConnection.getLocalPort()I',
    'bf.a()[B': 'jimm/comm/Icq$SOCKETConnection.getLocalIP()[B',
    'bf.run()V': 'jimm/comm/Icq$SOCKETConnection.run()V',
    'ap.a Ljava/lang/Thread;': 'jimm/comm/Icq$Connection.rcvThread Ljava/lang/Thread;',
    'ap.a Ljava/util/Vector;': 'jimm/comm/Icq$Connection.rcvdPackets Ljava/util/Vector;',
    'r.a()I': 'jimm/comm/Icq.getFlapSequence()I',
    'r.b()I': 'jimm/comm/Icq.getInitialFlapSequence()I',
    'r.a()Ljava/lang/Object;': 'jimm/comm/Icq.access$0()Ljava/lang/Object;',
    'co.c(Ljava/lang/String;)[B': 'jimm/comm/Util.ipToByteArray(Ljava/lang/String;)[B',
})

# Request-info state and all retained methods, confirmed by actual reply/clock execution.
SYMBOLS.update({
    'ce.c Z': 'jimm/comm/RequestInfoAction.infoShown Z',
    'ce.b Z': 'jimm/comm/RequestInfoAction.StartMainRequestInfo Z',
    'ce.d Z': 'jimm/comm/RequestInfoAction.showInfoText Z',
    'ce.a [I': 'jimm/comm/RequestInfoAction.indexCategories [I',
    'ce.b [I': 'jimm/comm/RequestInfoAction.codeIndexes [I',
    'ce.a [Ljava/lang/String;': 'jimm/comm/RequestInfoAction.interestNames [Ljava/lang/String;',
    'ce.b [Ljava/lang/String;': 'jimm/comm/RequestInfoAction.strData [Ljava/lang/String;',
    'ce.a Ljava/util/Date;': 'jimm/comm/RequestInfoAction.init Ljava/util/Date;',
    'ce.a I': 'jimm/comm/RequestInfoAction.packetCounter I',
    'ce.e Z': 'jimm/comm/RequestInfoAction.notFound Z',
    'ce.a Ljava/lang/String;': 'jimm/comm/RequestInfoAction.existingNick Ljava/lang/String;',
    'ce.b Ljava/lang/String;': 'jimm/comm/RequestInfoAction.uin_bDay Ljava/lang/String;',
    'ce.b I': 'jimm/comm/RequestInfoAction.day_bDay I',
    'ce.c I': 'jimm/comm/RequestInfoAction.month_bDay I',
    'ce.a()V': 'jimm/comm/RequestInfoAction.init()V',
    'ce.a(Lan;)Z': 'jimm/comm/RequestInfoAction.forward(Ljimm/comm/Packet;)Z',
    'ce.a(Ljava/lang/String;II)V': 'jimm/comm/RequestInfoAction.initInterestsDataItem(Ljava/lang/String;II)V',
    'ce.b(I)Ljava/lang/String;': 'jimm/comm/RequestInfoAction.getCategoriesString(I)Ljava/lang/String;',
    'ce.a(I)I': 'jimm/comm/RequestInfoAction.getSelectIndex(I)I',
    'ce.b(I)I': 'jimm/comm/RequestInfoAction.getCategoriesCode(I)I',
    'ce.a(I)Ljava/lang/String;': 'jimm/comm/RequestInfoAction.getCategoriesName(I)Ljava/lang/String;',
    'ce.a()Z': 'jimm/comm/RequestInfoAction.isCompleted()Z',
    'ce.b()Z': 'jimm/comm/RequestInfoAction.isError()Z',
    'r.a Z': 'jimm/comm/Icq.setPoint Z',
    'z.a(Ljava/lang/String;)V': 'jimm/ContactItem.rename(Ljava/lang/String;)V',
    'co.a$175c50c1(Ljava/io/DataInputStream;)I': 'jimm/comm/Util.getWord$175c50c1(Ljava/io/DataInputStream;)I',
    'co.a$6f0c2d54([B)Ljava/io/DataInputStream;': 'jimm/comm/Util.getDataInputStream$6f0c2d54([B)Ljava/io/DataInputStream;',
    'co.a(Ljava/io/DataInputStream;)Ljava/lang/String;': 'jimm/comm/Util.readAsciiz(Ljava/io/DataInputStream;)Ljava/lang/String;',
    'co.c(I)Ljava/lang/String;': 'jimm/comm/Util.genderToString(I)Ljava/lang/String;',
})

# Native XStatus catalog, GUID aliases and the real synchronized ContactItem caller.
CLASSES['bh'] = 'jimm/comm/GUID'
SYMBOLS.update({
    'bh.a [B': 'jimm/comm/GUID.guid [B',
    'co.a(Ljava/lang/String;CI)[B': 'jimm/comm/Util.explodeToBytes(Ljava/lang/String;CI)[B',
    'bj.a [Lbh;': 'jimm/comm/XStatus.xguids [Ljimm/comm/GUID;',
    'bj.a [Ljava/lang/String;': 'jimm/comm/XStatus.xstatus [Ljava/lang/String;',
    'bj.a Lf;': 'jimm/comm/XStatus.imageList LDrawControls/ImageList;',
    'bj.a I': 'jimm/comm/XStatus.index I',
    'bj.a(I)V': 'jimm/comm/XStatus.setStatusIndex(I)V',
    'bj.a(I)Lbh;': 'jimm/comm/XStatus.getStatusGUID(I)Ljimm/comm/GUID;',
    'bj.b()I': 'jimm/comm/XStatus.getStatusIndex()I',
    'z.a Lbj;': 'jimm/ContactItem.xstatus Ljimm/comm/XStatus;',
    'z.a([B)V': 'jimm/ContactItem.setXStatus([B)V',
    'z.a()Lbj;': 'jimm/ContactItem.getXStatus()Ljimm/comm/XStatus;',
})

# Concrete base action/message bodies; four abstract Action declarations have no instructions.
CLASSES['bk'] = 'jimm/comm/UrlMessage'
SYMBOLS.update({
    'aa.a Lr;': 'jimm/comm/Action.icq Ljimm/comm/Icq;',
    'aa.a Z': 'jimm/comm/Action.exclusive Z',
    'aa.b Z': 'jimm/comm/Action.executableConnected Z',
    'aa.a()Z': 'jimm/comm/Action.isCompleted()Z',
    'aa.a()I': 'jimm/comm/Action.getProgress()I',
    'aa.a()Ljava/lang/String;': 'jimm/comm/Action.getProgressMsg()Ljava/lang/String;',
    'aa.a(Lr;)V': 'jimm/comm/Action.setIcq(Ljimm/comm/Icq;)V',
    'aa.c()Z': 'jimm/comm/Action.isExecutable()Z',
    'r.b()Z': 'jimm/comm/Icq.isNotConnected()Z',
    'ac.b I': 'jimm/comm/Message.messageType I',
    'ac.b Z': 'jimm/comm/Message.offline Z',
    'ac.a Lz;': 'jimm/comm/Message.rcvr Ljimm/ContactItem;',
    'ac.a()Ljava/lang/String;': 'jimm/comm/Message.getRcvrUin()Ljava/lang/String;',
    'ac.a()Lz;': 'jimm/comm/Message.getRcvr()Ljimm/ContactItem;',
    'ac.a()Z': 'jimm/comm/Message.getOffline()Z',
    'av.a Ljava/lang/String;': 'jimm/comm/PlainMessage.text Ljava/lang/String;',
    'bk.a Ljava/lang/String;': 'jimm/comm/UrlMessage.url Ljava/lang/String;',
    'bk.b Ljava/lang/String;': 'jimm/comm/UrlMessage.text Ljava/lang/String;',
})

# Native password controller fields/calls, confirmed by real TextBox and failure-path execution.
CLASSES['by'] = 'jimm/EnterPassword'
SYMBOLS.update({
    'by.a Ljavax/microedition/lcdui/TextBox;': 'jimm/EnterPassword.passwordTextField Ljavax/microedition/lcdui/TextBox;',
    'by.a Ljavax/microedition/lcdui/Displayable;': 'jimm/EnterPassword._PreviousForm Ljavax/microedition/lcdui/Displayable;',
    'by.a Lby;': 'jimm/EnterPassword.instance Ljimm/EnterPassword;',
    'by.a(Ljavax/microedition/lcdui/Displayable;)V': 'jimm/EnterPassword.activate(Ljavax/microedition/lcdui/Displayable;)V',
    'by.a()V': 'jimm/EnterPassword.autoConnect()V',
    'cf.a Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdOk Ljavax/microedition/lcdui/Command;',
    'cf.b Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdCancel Ljavax/microedition/lcdui/Command;',
    'cv.d(Z)V': 'jimm/SplashCanvas.unlock(Z)V',
    'cv.d()Z': 'jimm/SplashCanvas.locked()Z',
    'cv.c Z': 'jimm/SplashCanvas.isLocked Z',
    'cv.a J': 'jimm/SplashCanvas.poundPressTime J',
    'jimm/Jimm.h Z': 'jimm/Jimm.isPasswordProtected Z',
    'jimm/Jimm.a Ljimm/Jimm;': 'jimm/Jimm.jimm Ljimm/Jimm;',
})

METHODS = [
    ('u', '<init>', '()V', '<init>'),
    ('u', 'b', '()V', 'reset'),
    ('u', 'a', '(Ljava/lang/String;)I', 'getNodeWeight'),
    ('u', 'a', '(Lax;Lax;)I', 'vtCompareNodes'),
    ('u', 'c', '()V', 'rebuildTree'),
    ('u', 'a', '(ZZ)V', 'setParameters'),
    ('u', 'a', '(Lk;)V', 'setListener'),
    ('u', 'a', '(Ljava/lang/String;)V', 'updateTreeCaptionAndCommands'),
    ('u', 'a', '(Lax;Lcn;)V', 'vtGetItemDrawData'),
    ('u', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('u', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('u', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('u', 'a', '()V', 'activate'),
    ('u', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('u', '<clinit>', '()V', '<clinit>'),
    ('b', '<init>', '()V', '<init>'),
    ('b', 'a', '(Ljava/lang/String;Z)[Ljava/lang/String;', 'getDirectoryContents'),
    ('b', 'a', '(Ljava/lang/String;)V', 'openFile'),
    ('b', 'a', '()Ljava/io/OutputStream;', 'openOutputStream'),
    ('b', 'a', '()Ljava/io/InputStream;', 'openInputStream'),
    ('b', 'a', '()V', 'close'),
    ('b', 'a', '()J', 'fileSize'),
    ('b', 'a', '()Ljava/lang/String;', 'getName'),

    ('af', '<init>', '(II)V', '<init>'),
    ('af', 'a', '(IIII)Z', 'pointerPressedOnUtem'),
    ('af', 'a', '(Ljavax/microedition/lcdui/Graphics;IIIIII)V', 'drawItemData'),
    ('af', 'a', '()V', 'showCurrName'),
    ('af', 'a', '(I)I', 'getItemHeight'),
    ('af', 'a', '()I', 'getSize'),
    ('af', 'm', '()I', 'getLength'),
    ('af', 'b', '()I', 'getCurrSelectedIdx'),
    ('af', 'b', '(I)I', 'getColorIndex'),
    ('af', 'a', '(ILcn;)V', 'get'),
    ('af', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('af', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('af', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('af', '<clinit>', '()V', '<clinit>'),

    ('aq', '<init>', '()V', '<init>'),
    ('aq', 'a', '(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V', 'selectTemplate'),
    ('aq', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('aq', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('aq', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('aq', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    # select is an instance method in May, but ProGuard statifies the rebuilt
    # method. Keep the authored 0.6 structure; TemplatesProbe covers its body.
    ('aq', 'c', '()V', 'refreshList'),
    ('aq', 'd', '()V', 'load'),
    ('aq', 'e', '()V', 'save'),
    ('aq', 'a', '()Ljava/lang/String;', 'getTemlate'),
    ('aq', 'f', '()V', 'addContextCommand'),
    ('aq', 'g', '()V', 'removeContextCommand'),
    ('aq', 'h', '()V', 'refresh'),
    ('aq', '<clinit>', '()V', '<clinit>'),

    ('ci', '<init>', '(Ljava/lang/Object;Ljava/lang/String;I)V', '<init>'),
    ('ci', 'd', '(Z)V', 'updateText'),
    ('ci', 'a', '(Ljavax/microedition/lcdui/Graphics;)V', 'paint'),
    ('ci', 'a', '(II)V', 'doKeyreaction'),
    ('ci', 'i', '(I)V', 'pointerDragged'),
    ('ci', 'a', '(ILcn;)V', 'get'),
    ('ci', 'a', '()I', 'getSize'),

    ('bi', '<init>', '(Ljava/lang/String;)V', '<init>'),
    ('bi', 'a', '()I', 'getSize'),
    ('bi', 'a', '(I)Lbm;', 'getLine'),
    ('bi', 'a', '(I)Z', 'isItemSelected'),
    ('bi', 'a', '(ILcn;)V', 'get'),
    ('bi', 'a', '()V', 'clear'),
    ('bi', 'a', '(Ljava/lang/String;IIIZC)V', 'internAdd'),
    ('bi', 'a', '(I)I', 'getItemHeight'),
    ('bi', 'a', '(Ljavax/microedition/lcdui/Graphics;IIIIII)V', 'drawItemData'),
    ('bi', 'a', '(IZ)V', 'moveCursor'),
    ('bi', 'a', '(IZI)Ljava/lang/String;', 'getTextByIndex'),
    ('bi', 'a', '(I)V', 'selectTextByIndex'),
    ('bi', 'a', '(IZ)Ljava/lang/String;', 'getCurrText'),
    ('bi', 'b', '()I', 'getCurrTextIndex'),
    ('bi', 'a', '(IIII)V', 'setColors'),
    ('bi', 'a', '(I)Lbi;', 'doCRLF'),
    ('bi', 'a', '(Le;Ljava/lang/String;I)Lbi;', 'addImage'),
    ('bi', 'm', '()I', 'getTextAreaWidth'),
    ('bi', 'a', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;', 'replace'),
    ('bi', 'a', '(Ljava/lang/String;IIII)V', 'addBigTextInternal'),
    ('bi', 'a', '(Ljava/lang/String;III)Lbi;', 'addBigText'),
    ('bc', '<init>', '()V', '<init>'),
    ('bc', 'a', '(I)I', 'getHeight'),
    ('bc', 'b', '(I)I', 'getWidth'),
    ('bm', '<init>', '()V', '<init>'),
    ('bm', 'a', '(I)Lbc;', 'elementAt'),
    ('bm', 'a', '(Lbc;)V', 'add'),
    ('bm', 'a', '(I)I', 'getHeight'),
    ('bm', 'b', '(I)I', 'getWidth'),
    ('bm', 'a', '(IILjavax/microedition/lcdui/Graphics;ILcd;)V', 'paint'),
    ('bm', 'a', '(Ljava/lang/StringBuffer;)V', 'readText'),

    ('d', '<init>', '(Ljava/lang/String;)V', '<init>'),
    ('d', 'a', '(Ljavax/microedition/lcdui/Item;)V', 'append'),
    ('d', 'a', '(Lax;Ljavax/microedition/lcdui/Item;)V', 'appendChoices'),
    ('d', 'a', '(Lax;Lcn;)V', 'vtGetItemDrawData'),
    ('d', 'a', '(IIII)Z', 'pointerPressedOnUtem'),
    ('d', 'a', '(Lax;Lax;)I', 'vtCompareNodes'),
    ('d', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('d', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('d', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('d', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('d', 'a', '(Lj;I)Le;', 'choiceImage'),
    ('d', 'a', '(Ljavax/microedition/lcdui/TextField;)Ljava/lang/String;', 'fieldText'),
    ('d', 'a', '(Ljava/lang/String;)Ljava/lang/String;', 'nonNull'),
    ('d', 'a', '(Ljava/lang/Object;)Ljava/lang/String;', 'getItemText'),
    ('d', 'a', '()V', 'clear'),
    ('c', '<init>', '(IIIIZ)V', '<init>'),
    ('c', 'a', '(Ljavax/microedition/lcdui/Graphics;II)V', 'drawByLeft'),
    ('c', 'a', '(I)V', 'setValue'),
    ('i', '<init>', '(Ljavax/microedition/lcdui/Item;Ljava/lang/String;ILe;)V', '<init>'),
    ('j', '<init>', '(Ljava/lang/String;I)V', '<init>'),
    ('j', '<init>', '(Ljava/lang/String;[Ljava/lang/String;)V', '<init>'),
    ('cn', '<init>', '()V', '<init>'),
    ('cn', 'a', '()V', 'clear'),

    ('aw', '<init>', '(Ljava/lang/String;Z)V', '<init>'),
    ('aw', 'a', '(I)Lax;', 'getDrawItem'),
    ('aw', 'a', '()V', 'checkToRebuildTree'),
    ('aw', 'a', '(I)V', 'setStepSize'),
    ('aw', 'a', '()Lax;', 'getCurrentItem'),
    ('aw', 'a', '(Lax;)V', 'setCurrentItem'),
    ('aw', 'a', '(Ljava/util/Vector;Lax;Lax;)Z', 'buildNodePath'),
    ('aw', 'a', '()Z', 'itemSelected'),
    ('aw', 'a', '(IIII)Z', 'pointerPressedOnUtem'),
    ('aw', 'a', '()I', 'getSize'),
    ('aw', 'n', '()V', 'rebuildTreeIntItems'),
    ('aw', 'b', '(Lax;I)V', 'fillTreeIntItems'),
    ('aw', 'a', '(ILcn;)V', 'get'),
    ('aw', 'a', '(Ljavax/microedition/lcdui/Graphics;IIIIII)V', 'drawItemData'),
    ('aw', 'a', '(Lax;Ljava/lang/Object;)Lax;', 'addNode'),
    ('aw', 'a', '(Lax;Lax;)Lax;', 'findParent'),
    ('aw', 'a', '(Lax;)Z', 'removeNode'),
    ('aw', 'b', '(Lax;)V', 'sortNode'),
    ('aw', 'a', '(Lax;Lax;I)V', 'insertChild'),
    ('aw', 'a', '(Lax;I)V', 'deleteChild'),
    ('aw', 'b', '(Lax;Lax;)I', 'getIndexOfChild'),
    ('aw', 'a', '(Lax;Z)V', 'setExpandFlag'),
    ('aw', 'b', '()V', 'clear'),
    ('aw', 'o', '()V', 'storeLastNode'),
    ('aw', 'c', '()V', 'afterUnlock'),
    ('aw', 'p', '()V', 'restoreLastNode'),
    ('ax', '<init>', '(Ljava/lang/Object;)V', '<init>'),
    ('ax', 'a', '()V', 'clear'),
    ('ax', 'a', '()Z', 'getExpanded'),
    ('ax', 'a', '()I', 'size'),
    ('ax', 'a', '(I)Lax;', 'elementAt'),
    ('ax', 'a', '(Lax;)Lax;', 'addItem'),
    ('ax', 'a', '(Lax;I)V', 'insertChild'),
    ('ax', 'a', '(I)V', 'removeItem'),
    ('ax', 'a', '(Lax;)I', 'findItem'),
    ('ax', 'a', '(Ljava/util/Vector;Lax;Laz;)I', 'getInsertionPos'),
    ('ax', 'a', '(Laz;)V', 'sort'),

    ('m', 'a', '(Lax;Lax;)I', 'vtCompareNodes'),
    ('m', 'i', '()V', 'sortAll'),
    ('m', 'j', '()V', 'buildTree'),
    ('m', 'a', '(Lax;Lbb;)V', 'calcGroupData'),
    ('m', 'a', '(Lz;ZZ)V', 'contactChanged'),
    ('m', 'c', '()V', 'beforeConnect'),
    ('m', 'd', '()V', 'setStatusesOffline'),
    ('m', 'e', '()V', 'resetAutoAnsweredFlag'),
    ('m', 'a', '(II[Lbs;)V', 'update'),
    ('m', 'a', '(Ljava/lang/String;II[B[BIIIIIIII)V', 'update'),
    ('m', 'a', '(Lz;ZZI)V', 'statusChanged'),
    ('m', 'a', '(Lz;)V', 'removeContactItem'),
    ('m', 'b', '(Lz;)V', 'addContactItem'),
    ('m', 'a', '(Lbb;)V', 'addGroup'),
    ('m', 'b', '(Lbb;)V', 'removeGroup'),
    ('m', 'a', '(I)[Lz;', 'getGroupItems'),
    ('m', 'a', '(I)Lbb;', 'getGroupById'),
    ('m', 'a', '(Ljava/lang/String;)Lz;', 'getItembyUIN'),

    ('cd', 'd', '()V', 'assignSoftKeys'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;IIIIII)V', 'drawItemData'),
    ('cd', 'b', '(Ljavax/microedition/lcdui/Graphics;IIII)Z', 'drawMenuItems'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;Ljava/util/Vector;IIIII)Z', 'drawMenuItems'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;IIIIIIIII)Z', 'paint3points'),
    ('cd', 'b', '(Ljava/util/Vector;)V', 'sortMenuItems'),
    ('cd', 'a', '(Ljava/util/Vector;)V', 'initPopupMenuItems'),
    ('cd', 'a', '(IIZ)V', 'moveSelectedMenuItem'),
    ('cd', 'a', '()Ljava/util/Vector;', 'leftMenuPressed'),
    ('cd', 'b', '()Ljava/util/Vector;', 'rightMenuPressed'),
    ('cd', 'c', '(II)V', 'keyReaction'),
    ('cd', 'e', '(I)I', 'getMenuHeight'),
    ('m', '<init>', '()V', '<init>'),
    ('m', 'a', '()V', 'activate'),
    ('m', 'a', '(Lbb;)Lax;', 'addGroupNodeInternal'),
    ('m', 'a', '(Lax;Lcn;)V', 'vtGetItemDrawData'),
    ('m', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('ax', 'a', '(Z)V', 'setExpanded'),
    ('jimm/Jimm', 'startApp', '()V', 'startApp'),
    ('bb', 'a', '(II)V', 'setCounters'),
    ('bb', 'b', '(II)V', 'updateCounters'),
    ('bb', 'd', '()I', 'getImageIndex'),
    ('bb', 'a', '()Lbj;', 'getXStatus'),
    ('bb', 'c', '()Ljava/lang/String;', 'getText'),
    ('bb', 'a', '()I', 'getTextColor'),
    ('bb', 'i', '()I', 'getClientImageIndex'),
    ('bb', 'f', '()I', 'getBirthDayImageIndex'),
    ('bb', 'g', '()I', 'getHappyImageIndex'),
    ('bb', 'h', '()I', 'getAuthImageIndex'),
    ('bb', 'j', '()I', 'getVisibilityImageIndex'),
    ('bb', 'k', '()I', 'getIgnoreImageIndex'),
    ('bb', 'b', '()Ljava/lang/String;', 'getName'),
    ('bb', 'equals', '(Ljava/lang/Object;)Z', 'equals'),
    ('bb', 'b', '()I', 'getFontStyle'),
    ('bb', 'a', '()Ljava/lang/String;', 'getSortText'),
    ('bb', 'a', '(I)I', 'getSortWeight'),
    ('bb', '<init>', '(ILjava/lang/String;)V', '<init>'),
    ('bb', '<init>', '()V', '<init>'),
    ('bb', '<init>', '(Ljava/lang/String;)V', '<init>'),

    # drawCaption is inlined; VirtualListProbe checks its pixels and recorded bounds.
    ('cd', 'g', '()I', 'getNonScrollerArea'),
    ('cd', 'i', '(I)V', 'pointerDragged'),
    ('cd', 'a', '(IIII)Z', 'pointerPressedOnUtem'),
    ('cd', 'b', '(II)V', 'pointerPressed'),
    ('cd', 'm', '()I', 'getCapHeight'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;IIII)V', 'drawScroller'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;IIIIII)Z', 'drawItems'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;III)V', 'paintAllOnGraphics'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;)V', 'paint'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;IIII)Z', 'drawMenuBar'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;IIIIIII)V', 'drawGradient'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;IIII)V', 'drawString'),
    ('am', 'pointerReleased', '(II)V', 'pointerReleased'),

    ('ag', '<init>', '()V', '<init>'),
    ('ag', '<clinit>', '()V', '<clinit>'),
    ('ag', 'a', '()Le;', 'getXStatusImage'),
    ('ag', 'b', '()Le;', 'getPrivateStatusImage'),
    ('ag', 'a', '(Z)Le;', 'getSoundImage'),
    ('ag', 'a', '()V', 'build'),
    ('ag', 'b', '()V', 'activate'),
    ('ag', 'a', '(Ljava/lang/String;)V', 'activate'),
    ('ag', 'a$78a4d1d0', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V', 'showTextBoxForm'),
    ('ag', 'c', '()V', 'initStatusList'),
    # showPrivateStatus is inlined in this source build; MenuProbe checks the real controller.
    ('ag', 'a', '(I)V', 'setOnlineStatus'),
    ('ag', 'a', '(I)I', 'statusMsgIdxSelector'),
    ('ag', 'b', '(I)V', 'CLManagementItemSelected'),
    ('ag', 'f', '()V', 'actionMMCLAct'),
    ('ag', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    # Accessors and flashRestoreOldCaption are inlined in this optimized source JAR;
    # their behavior is checked against unoptimized source by TimerProbe.
    ('at', '<init>', '(Laa;)V', '<init>'),
    ('at', '<init>', '(I)V', '<init>'),
    ('at', '<init>', '(Ljava/lang/Object;Ljava/lang/String;II)V', '<init>'),
    ('at', '<clinit>', '()V', '<clinit>'),
    ('at', 'a', '()V', 'setStatusTimer'),
    ('at', 'a', '(I)V', 'statusChange'),
    ('at', 'b', '()Z', 'checkFlashControlIsActive'),
    ('cg', '<init>', '()V', '<init>'),
    ('cg', 'a', '()V', 'callColorSchemeOptions'),
    ('cg', 'a', '(Ljava/lang/String;IZ)Ljava/lang/String;', 'getHotKeyActName'),
    ('cg', 'd', '()V', 'InitHotkeyMenuUI'),
    ('cg', 'b', '()V', 'InitColorMenuUI'),
    ('cg', 'e', '()V', 'saveColors'),
    ('cg', 'f', '()V', 'restoreColors'),
    ('cg', 'g', '()V', 'readAccontsData'),
    ('cg', 'a', '(Ljava/lang/String;)Ljava/lang/String;', 'checkUin'),
    ('cg', 'h', '()V', 'showAccountControls'),
    ('cg', 'i', '()V', 'setAccountOptions'),
    ('cg', 'j', '()V', 'readAccontsControls'),
    ('cg', 'itemStateChanged', '(Ljavax/microedition/lcdui/Item;)V', 'itemStateChanged'),
    ('cg', 'c', '()V', 'activate'),
    ('cg', 'a', '(Ljavax/microedition/lcdui/ChoiceGroup;Ljava/lang/String;)V', 'addStr'),
    ('cg', 'a', '(Ljavax/microedition/lcdui/ChoiceGroup;Ljava/lang/String;I)V', 'setChecked'),
    ('cg', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('cg', 'run', '()V', 'run'),
    ('cg', 'a', '(Ljava/lang/String;)V', 'onFileSelect'),
    ('cg', 'b', '(Ljava/lang/String;)V', 'onDirectorySelect'),
    ('cg', 'a', '()Lz;', 'getCItem'),
    ('cg', 'k', '()V', 'setStatusAfterChanges'),
    ('cg', 'l', '()V', 'clearForm'),
    ('cf', 'd', '()V', 'clearAll'),
    ('bi', 'b', '(I)I', 'getTextIndex'),
    ('cf', 'a$1385ff', '()V', 'setColorScheme'),
    ('ar', '<init>', '(II)V', '<init>'),
    ('ar', 'keyPressed', '(I)V', 'keyPressed'),
    ('ar', 'paint', '(Ljavax/microedition/lcdui/Graphics;)V', 'paint'),
    ('z', 'c', '()V', 'prepareToBlink'),
    ('z', 'b', '(Z)V', 'startBlinking'),
    ('z', 'b', '()I', 'getFontStyle'),
    ('z', 'a', '()I', 'getTextColor'),
    ('ch', '<init>', '(Lz;)V', '<init>'),
    ('ch', 'run', '()V', 'run'),
    ('q', '<init>', '()V', '<init>'),
    ('q', '<clinit>', '()V', '<clinit>'),
    ('q', 'a', '(Ljava/lang/String;Ljava/lang/String;BLjava/lang/String;J)V', 'addText'),
    ('q', 'a', '()Ljavax/microedition/rms/RecordStore;', 'getRS'),
    ('q', 'a', '(Ljava/lang/String;)Ljava/lang/String;', 'getRSName'),
    ('q', 'c', '(Ljava/lang/String;)V', 'openUINRecords'),
    ('q', 'a', '(Ljava/lang/String;)I', 'getRecordCount'),
    ('q', 'a', '(Ljava/lang/String;I)Lbz;', 'getRecord'),
    ('q', 'b', '(Ljava/lang/String;I)Lbz;', 'getCachedRecord'),
    ('q', 'a', '(Ljava/lang/String;Ljava/lang/String;)V', 'showHistoryList'),
    ('q', 'a', '(Ljava/lang/String;)V', 'clearHistory'),
    ('q', 'a', '()V', 'clearCache'),
    ('q', 'b', '()V', 'setColorScheme'),
    ('q', 'a', '(Ljava/lang/String;Ljava/lang/String;ZZ)Z', 'find_intern'),
    ('q', 'a', '(Ljava/lang/String;Ljava/lang/String;ZZ)V', 'find'),
    ('q', 'b', '(Ljava/lang/String;)V', 'clear_all'),
    ('bg', '<init>', '()V', '<init>'),
    ('bg', '<clinit>', '()V', '<clinit>'),
    ('bg', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('bg', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('bg', 'd', '(Z)V', 'copyText'),
    ('bg', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('bg', 'a', '(I)V', 'moveInList'),
    ('bg', 'a', '()Lz;', 'getCItem'),
    ('bg', 'd', '(Ljava/lang/String;)V', 'export'),
    ('bg', 'a', '(Ljava/lang/String;)V', 'onFileSelect'),
    ('bg', 'b', '(Ljava/lang/String;)V', 'onDirectorySelect'),
    ('bg', 'run', '()V', 'run'),
    ('bg', 'a', '([Lz;)V', 'startExport'),
    ('bg', 'a', '(Ljava/lang/String;)Lw;', 'openFile'),
    ('bg', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('bg', 'a', '()V', 'showMessText'),
    ('bg', 'a', '()Ljava/lang/String;', 'getCurrUin'),
    ('bg', 'a$16da05f7', '(Ljava/lang/String;)V', 'setCurrUin'),
    ('bg', 'a', '()I', 'getSize'),
    ('bg', 'a', '(ILcn;)V', 'get'),

    # ChatTextList appends/acks/accessors are inlined in the rebuilt JAR; ChatProbe exercises them.
    ('y', 'a', '(Lz;)V', 'buildMenu'),
    ('y', 'a', '()Z', 'isVisible'),
    ('y', 'a', '()V', 'removeAuthCommands'),
    ('y', 'a', '(Z)I', 'getInOutColor'),
    ('y', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('y', 'b', '()V', 'checkTextForURL'),
    ('y', 'c', '()V', 'checkForAuthReply'),
    ('y', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('y', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('y', 'e', '()V', 'activate'),
    ('y', '<init>', '(Ljava/lang/String;Lz;)V', '<init>'),
    ('y', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('y', '<clinit>', '()V', '<clinit>'),
    ('bt', 'a', '(Lz;Lac;)V', 'addMessage'),
    ('bt', 'a', '(Ljava/lang/String;JZ)V', 'AckMessage'),
    ('bt', 'a', '(Lz;Ljava/lang/String;JLjava/lang/String;J)V', 'addMyMessage'),
    ('bt', 'a', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZZLe;J)V', 'addTextToForm'),
    ('bt', 'a', '(Ljava/lang/String;Ljava/lang/String;)V', 'copyText'),
    ('bt', 'a', '(Ljava/lang/String;)Ly;', 'getChatHistoryAt'),
    ('bt', 'a', '(Ljava/lang/String;)V', 'chatHistoryDelete'),
    ('bt', 'a', '(Ljava/lang/String;I)V', 'chatHistoryDelete'),
    ('bt', 'a', '(Ljava/lang/String;)Z', 'chatHistoryShown'),
    ('bt', 'b', '(Ljava/lang/String;)Z', 'chatHistoryExists'),
    ('bt', 'a', '(Lz;Ljava/lang/String;)V', 'newChatForm'),
    ('bt', 'a', '(Lz;)V', 'fillFormHistory'),
    ('bt', 'b', '(Ljava/lang/String;Ljava/lang/String;)V', 'contactRenamed'),
    ('bt', 'b', '(Ljava/lang/String;)V', 'UpdateCaption'),
    ('bt', 'a', '()V', 'setColorScheme'),
    ('bt', 'c', '(Ljava/lang/String;)V', 'calcCounter'),
    ('bt', 'a', '(Lz;)Z', 'activateIfExists'),
    ('bt', 'b', '(Lz;)V', 'removeAuthCommands'),
    ('bt', '<init>', '()V', '<init>'),
    ('bt', '<clinit>', '()V', '<clinit>'),
    ('ca', 'a', '()Z', 'getIncoming'),
    ('ca', '<init>', '(ZJIZ)V', '<init>'),
    ('bo', 'a', '(Ljava/util/Vector;Ljava/lang/String;Ljava/lang/Integer;)V', 'insertTextCorr'),
    ('bo', 'a', '(Ljava/io/DataInputStream;)Ljava/lang/String;', 'readLineFromStream'),
    ('ai', '<clinit>', '()V', '<clinit>'),
    ('ai', 'a', '(Ljava/lang/String;)V', 'setCurrUiLanguage'),
    ('ai', 'a', '()V', 'loadLang'),
    ('ai', 'a', '(Ljava/lang/String;)Ljava/lang/String;', 'getString'),
    ('ai', 'a$7a1ba7c4', '(Ljava/lang/String;)Ljava/lang/String;', 'getString$7a1ba7c4'),
    ('bo', '<clinit>', '()V', '<clinit>'),
    ('bo', '<init>', '()V', '<init>'),
    ('bo', 'a', '(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V', 'selectEmotion'),
    ('bo', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('bo', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('bo', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('bo', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('bo', 'a', '()V', 'select'),
    ('bo', 'a', '(Lbi;Ljava/lang/String;III)V', 'addTextWithEmotions'),

    ('bl', '<init>', '()V', '<init>'),
    ('bl', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('bl', 'a', '(I)Ljava/lang/String;', 'getRecordDesc'),
    ('bl', 'a', '()Ljava/lang/String;', 'saveInLine'),
    ('bl', 'a', '([B)V', 'LoadLineInTable'),
    ('ag', 'd', '()V', 'showXStatusSelector'),
    # TrafficScreen.update is specialized to ()V; TrafficProbe exercises the actual update(true) callers.
    ('x', '<init>', '()V', '<init>'),
    ('x', 'a', '()V', 'save'),
    ('x', 'a', '(I)Ljava/lang/String;', 'getTrafficString'),
    ('x', 'a', '()I', 'getSessionTraffic'),
    ('x', 'a', '(I)V', 'addInTraffic'),
    ('x', 'b', '(I)V', 'addOutTraffic'),
    ('a', '<init>', '()V', '<init>'),
    ('a', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),

    ('cl', '<init>', '(Lac;)V', '<init>'),
    # getMsgId is inlined in the rebuilt JAR; OutgoingProbe checks its ID arithmetic.
    ('cl', 'b', '()I', 'getMsgCounter'),
    ('cl', 'a', '()V', 'init'),
    ('cl', 'a', '(Lan;)Z', 'forward'),
    ('cl', 'a', '()Z', 'isCompleted'),
    ('cl', 'b', '()Z', 'isError'),
    ('cl', '<clinit>', '()V', '<clinit>'),

    ('cc', '<init>', '(Lp;)V', '<init>'),
    ('cc', 'b', '()V', 'reset'),
    ('cc', 'paint', '(Ljavax/microedition/lcdui/Graphics;)V', 'paint'),
    ('cc', 'a', '(Ljava/lang/String;)V', 'createPlayer'),
    ('cc', 'a', '()V', 'start'),
    ('cc', 'a', '(Ljava/lang/String;)[B', 'getSnapshot'),
    ('cc', 'c', '()V', 'takeSnapshot'),
    ('cc', 'd', '()V', 'stop'),
    ('cc', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('cc', 'e', '()V', 'openSendScreen'),
    ('cc', 'keyPressed', '(I)V', 'keyPressed'),
    ('bw', '<init>', '(Lab;)V', '<init>'),
    ('bw', 'a', '()V', 'init'),
    ('bw', 'a', '(Lan;)Z', 'forward'),
    ('bw', 'a', '()I', 'getProgress'),
    ('bw', 'a', '()Z', 'isCompleted'),
    ('bw', 'b', '()Z', 'isError'),
    ('bw', 'a', '(I)V', 'onEvent'),
    ('ao', '<init>', '()V', '<init>'),
    ('ao', 'a', '(Ljava/lang/String;)V', 'connect'),
    ('ao', 'a', '()V', 'close'),
    ('ao', 'a', '()I', 'available'),
    ('ao', 'a', '()Lan;', 'getPacket'),
    ('ao', 'a', '(Lan;)V', 'sendPacket'),
    ('ao', 'b', '()I', 'getLocalPort'),
    ('ao', 'a', '()[B', 'getLocalIP'),
    ('ao', 'run', '()V', 'run'),
    ('au', '<init>', '([B)V', '<init>'),
    ('au', 'a', '()[B', 'toByteArray'),
    ('ab', '<init>', '(Ljava/lang/String;Lz;Ljava/lang/String;Ljava/lang/String;Ljava/io/InputStream;I)V', '<init>'),
    ('ab', 'a', '(I)[B', 'getFileSegmentPacket'),

    ('p', '<init>', '(ILz;)V', '<init>'),
    ('p', 'a', '()Lz;', 'getCItem'),
    ('p', 'a', '(Ljava/io/InputStream;I)V', 'setData'),
    ('p', 'a', '()V', 'startFT'),
    ('p', 'run', '()V', 'run'),
    ('p', 'a', '(Ljava/lang/String;)V', 'onFileSelect'),
    ('p', 'b', '(Ljava/lang/String;)V', 'onDirectorySelect'),
    ('p', 'itemStateChanged', '(Ljavax/microedition/lcdui/Item;)V', 'itemStateChanged'),
    ('p', 'a', '(Ljava/lang/String;Ljava/lang/String;)V', 'askForNameDesc'),
    ('p', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('p', 'b', '()V', 'free'),
    ('x', 'b', '(I)Ljava/lang/String;', 'getString'),
    ('x', 'a', '(Z)I', 'generateCostSum'),

    ('cp', '<init>', '(Z)V', '<init>'),
    ('cp', 'a', '(I)[Ljava/lang/String;', 'getResult'),
    ('cp', 'a', '()Lcr;', 'getSearchForm'),
    ('cr', '<init>', '(Lcp;)V', '<init>'),
    ('cr', 'a', '(I)V', 'activate'),
    ('cr', 'a', '(Z)V', 'nextOrPrev'),
    ('cr', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('cr', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('cr', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('cr', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('o', '<init>', '(Lcp;[Ljava/lang/String;)V', '<init>'),
    ('o', 'a', '()V', 'init'),
    ('o', 'a', '(Lan;)Z', 'forward'),
    ('o', 'a', '(I)V', 'onEvent'),
    ('o', 'a', '()Z', 'isCompleted'),
    ('o', 'b', '()Z', 'isError'),

    ('cf', 'a', '(Lz;)V', 'showContactMenu'),
    ('cf', 'b', '(Lz;)V', 'addUser'),
    ('cf', 'c', '(Lz;)V', 'showClientInfo'),
    ('m', 'b', '(Z)V', 'afterConnect'),
    ('cf', 'b', '()V', 'about'),
    ('cf', 'a', '(Lbi;Ljava/lang/String;Le;IZ)V', 'addTextListItem'),
    ('cf', 'a', '(Lbi;Ljava/lang/String;Le;IZZ)V', 'addTextListItem'),
    ('g', 'run', '()V', 'run'),

    ('br', '<init>', '([Ljava/lang/String;Ljava/lang/Object;)V', '<init>'),
    ('br', 'run', '()V', 'run'),
    ('br', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('as', 'a', '()V', 'init'),
    ('as', 'a', '(Lan;)Z', 'forward'),
    ('as', 'a', '()Z', 'isCompleted'),
    ('as', 'b', '()Z', 'isError'),
    ('as', 'a', '()I', 'getProgress'),
    ('as', 'a', '(I)V', 'onEvent'),
    ('cf', 'a', '(Ljava/lang/String;Ljava/lang/String;)V', 'requiestUserInfo'),
    ('cf', 'a', '([Ljava/lang/String;Lbi;)V', 'fillUserInfo'),
    ('cf', 'a', '([Ljava/lang/String;)V', 'showUserInfo'),

    ('cf', 'a', '()Z', 'clipBoardIsEmpty'),
    ('cf', 'a', '(Z)Ljava/lang/String;', 'getClipBoardText'),
    ('cf', 'c', '()V', 'clearClipBoardText'),
    ('cf', 'a', '(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V', 'setClipBoardText'),
    ('cf', 'a', '(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;', 'insertQuotingChars'),
    ('ah', 'a', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V', 'registerAction'),
    ('ah', 'a', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V', 'addAction'),
    ('ah', 'a', '(Ljava/lang/String;Ljava/lang/String;Z)V', 'addAction'),
    ('cd', 'a', '(I)Ljavax/microedition/lcdui/Command;', 'findMenuByType'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Command;I)V', 'addCommandEx'),
    ('cd', 'a', '(Ljavax/microedition/lcdui/Command;)V', 'removeCommandEx'),
    ('cd', 'm', '()V', 'removeAllCommands'),

    ('co', 'a', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;', 'replaceStr'),
    ('ae', 'a', '(Lan;)V', 'forward'),
    ('ae', 'a', '(Lz;)V', 'sendAutoMessage'),
    ('ae', 'a', '(Lac;)Z', 'isSpam'),
    ('co', 'b', '()I', 'createRandomId'),
    ('co', 'a', '()I', 'getCounter'),
    ('ct', 'a', '()V', 'init'),
    ('ct', 'a', '(Lan;)Z', 'forward'),
    ('ct', 'a', '()Z', 'isCompleted'),
    ('ct', 'b', '()Z', 'isError'),
    ('z', 'a', '(I[B)V', 'setIPValue'),
    ('z', 'a', '(I)[B', 'getIPValue'),
    ('ct', 'a', '(Lz;I)[B', 'packRosterItem'),
    ('n', 'a', '()V', 'init'),
    ('n', 'a', '(Lan;)Z', 'forward'),
    ('r', 'b', '(I)V', 'setXStatus'),
    ('bq', 'a', '(Z)V', 'setStandartUserInfo'),
    ('co', 'c', '([BII)Ljava/lang/String;', 'detectClientVersion'),
    ('co', 'a', '(I)I', 'translateStatusSend'),
    ('co', 'a', '(ILz;)I', 'translateStatusReceived'),
    ('cj', 'a', '(Ljava/io/DataInputStream;)V', 'readOptions'),
    ('cj', 'a', '(Ljava/io/DataOutputStream;)V', 'writeOptions'),
    ('z', 'a', '(I)I', 'getSortWeight'),
    ('ci', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('ag', 'a', '(ZZ)V', 'doExit'),
    ('aq', 'a', '()V', 'clearTemplates'),
    ('r', 'a', '()I', 'getFlapSequence'),
    ('r', 'b', '()I', 'getInitialFlapSequence'),
    ('r', 'a', '()Z', 'isDisconnected'),
    ('r', 'b', '(Z)V', 'setDisconnected'),
    ('r', 'a', '(I)Z', 'isNotCriticalConnectionError'),
    ('r', 'a', '(Z)V', 'disconnect'),
    ('r', 'e', '()V', 'resetServerCon'),
    ('n', 'a', '()Z', 'isCompleted'),
    ('n', 'b', '()Z', 'isError'),
    ('ap', 'a', '(Z)V', 'setInputCloseFlag'),
    ('ap', 'a', '()Z', 'getInputCloseFlag'),
    ('bv', 'a', '(Lbv;)V', 'handleException'),
    ('cv', 'a', '(Ljava/lang/String;)V', 'setMessage'),
    ('cv', 'a', '(I)V', 'setStatusToDraw'),
    ('cv', 'f', '(Le;)V', 'setXStatusToDraw'),
    ('cv', 'l', '(I)V', 'setProgress'),
    ('cv', 'b', '(Ljavax/microedition/lcdui/Command;)V', 'addCmd'),
    ('cv', 'c', '(Ljavax/microedition/lcdui/Command;)V', 'removeCmd'),
    ('cv', 'b', '(Ljavax/microedition/lcdui/CommandListener;)V', 'setCmdListener'),
    ('cv', 'd', '()Z', 'locked'),
    ('cv', 'o', '()V', 'messageAvailable'),
    ('cv', 'n', '()V', 'lockScreen'),
    ('cv', 'd', '(Z)V', 'unlock'),
    ('cv', 'a', '(Ljava/lang/String;Laa;Z)V', 'addTimerTask'),
    ('cv', 'r', '()V', 'cancelActionTimer'),
    ('cv', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('at', 'cancel', '()Z', 'cancel'),
    ('at', 'run', '()V', 'run'),
    ('ay', 'a', '(Ljava/lang/String;)V', 'connect'),
    ('ay', 'a', '(Lan;[BII)V', 'sendPacket'),
    ('ay', 'a', '(Lan;)V', 'sendPacket'),
    ('ay', 'run', '()V', 'run'),
    ('ay', 'b', '()V', 'stream_close'),
    ('ay', 'a', '()V', 'close'),
    ('cb', 'a', '(Ljava/lang/String;)V', 'connect'),
    ('cb', 'a', '(BLjava/lang/String;Ljava/lang/String;)V', 'connect_socks'),
    ('cb', 'b', '()V', 'stream_close'),
    ('cb', 'a', '()V', 'close'),
]


SYMBOLS.update({'co.a([BII)V': 'jimm/comm/Util.putByte([BII)V', 'co.b([BII)V': 'jimm/comm/Util.putWord([BII)V', 'co.a([BIIZ)V': 'jimm/comm/Util.putWord([BIIZ)V', 'co.a([BIJ)V': 'jimm/comm/Util.putDWord([BIJ)V', 'co.a([BIJZ)V': 'jimm/comm/Util.putDWord([BIJZ)V', 'co.a(Ljava/lang/String;)[B': 'jimm/comm/Util.stringToByteArray(Ljava/lang/String;)[B'})
METHODS.extend([
    ('co', 'a', '([BII)V', 'putByte'),
    ('co', 'b', '([BII)V', 'putWord'),
    ('co', 'a', '([BIIZ)V', 'putWord'),
    ('co', 'a', '([BIJ)V', 'putDWord'),
    ('co', 'a', '([BIJZ)V', 'putDWord'),
    ('co', 'a', '(Ljava/lang/String;)[B', 'stringToByteArray'),
    ('an', 'a', '(I)V', 'setSequence'),
    ('an', '<init>', '()V', '<init>'),
    ('an', '<init>', '([B)V', '<init>'),
    ('an', 'a', '()[B', 'toByteArray'),
    ('ak', '<init>', '(IIIIJ[B[B)V', '<init>'),
    ('ak', '<init>', '(IIJ[B[B)V', '<init>'),
    ('ak', 'a', '()I', 'getFamily'),
    ('ak', 'b', '()I', 'getCommand'),
    ('ak', 'b', '()[B', 'getData'),
    ('ak', 'a', '()[B', 'toByteArray'),
    ('h', '<init>', '(I)V', '<init>'),
    ('h', '<init>', '()V', '<init>'),
    ('h', '<init>', '(I[B)V', '<init>'),
    ('h', '<init>', '([B)V', '<init>'),
    ('h', '<init>', '(ILjava/lang/String;Ljava/lang/String;)V', '<init>'),
    ('h', '<init>', '(Ljava/lang/String;Ljava/lang/String;)V', '<init>'),
    ('h', 'a', '()I', 'getType'),
    ('h', 'a', '()[B', 'toByteArray'),
    ('bn', '<init>', '(ILjava/lang/String;Ljava/lang/String;[B)V', '<init>'),
    ('bn', '<init>', '(IILjava/lang/String;)V', '<init>'),
    ('bn', '<init>', '(I)V', '<init>'),
    ('bn', '<init>', '()V', '<init>'),
    ('bn', 'a', '()I', 'getType'),
    ('bn', 'b', '()I', 'getError'),
    ('bn', 'a', '()[B', 'toByteArray'),
    ('bu', '<init>', '(IJIILjava/lang/String;I[B[B)V', '<init>'),
    ('bu', '<init>', '(Ljava/lang/String;I[B[B)V', '<init>'),
    ('bu', 'a', '()[B', 'toByteArray'),
    ('ck', '<init>', '(IJIILjava/lang/String;I[B[B)V', '<init>'),
    ('ck', 'a', '()[B', 'toByteArray'),
])


METHODS.extend([
    ('cq', '<init>', '(ILz;)V', '<init>'),
    ('cq', 'a', '()V', 'init'),
    ('cq', 'a', '(Lan;)Z', 'forward'),
    ('cq', 'a', '()Z', 'isCompleted'),
    ('cq', 'b', '()Z', 'isError'),
    ('cq', 'a', '()I', 'getProgress'),
    ('al', '<init>', '(Ljava/lang/String;)V', '<init>'),
    ('al', 'a', '()V', 'init'),
    ('al', 'a', '(Lan;)Z', 'forward'),
    ('al', 'a', '()Z', 'isCompleted'),
    ('al', 'b', '()Z', 'isError'),
    ('v', '<init>', '(Ls;)V', '<init>'),
    ('v', 'a', '()V', 'init'),
    ('v', 'a', '(Lan;)Z', 'forward'),
    ('v', 'a', '()Z', 'isCompleted'),
    ('v', 'b', '()Z', 'isError'),
    ('s', '<init>', '(ILjava/lang/String;ZLjava/lang/String;)V', '<init>'),
    ('co', 'a', '(Ljava/io/ByteArrayOutputStream;IZ)V', 'writeWord'),
    ('co', 'a', '(Ljava/io/ByteArrayOutputStream;Ljava/lang/String;Z)V', 'writeLenAndString'),
    ('co', 'a', '(Ljava/lang/String;Z)[B', 'stringToByteArray'),
    ('co', 'a$1385f3', '()J', 'createCurrentDate$1385f3'),
])


SYMBOLS.update({'r.a Lap;': 'jimm/comm/Icq.c Ljimm/comm/Icq$Connection;', 'ap.a(Lan;)V': 'jimm/comm/Icq$Connection.sendPacket(Ljimm/comm/Packet;)V', 'cj.a(I)Ljava/lang/String;': 'jimm/Options.getString(I)Ljava/lang/String;', 'co.a([BIZ)I': 'jimm/comm/Util.getWord([BIZ)I'})
METHODS.extend([('cj', 'a', '(I)Ljava/lang/String;', 'getString'), ('co', 'a', '([BIZ)I', 'getWord')])


METHODS.extend([
    ('l', '<init>', '(I[Ljava/lang/Object;)V', '<init>'),
    ('l', 'run', '()V', 'run'),
    ('l', 'a', '(I[Ljava/lang/Object;)V', 'callSerially'),
    ('l', 'a', '(ILjava/lang/Object;)V', 'callSerially'),
    ('l', 'a', '(Lac;)V', 'addMessageSerially'),
    ('l', 'a', '(Ljava/lang/String;Z)V', 'BeginTyping'),
    ('l', 'a$4e9ee315', '([Ljava/lang/Object;)Z', 'getBoolean$4e9ee315'),
    ('l', 'a', '([Ljava/lang/Object;II)V', 'setInt'),
    ('l', 'a', '([Ljava/lang/Object;I)I', 'getInt'),
    ('cj', 'a', '(I)I', 'getInt'),
    ('cj', 'a', '(I)Z', 'getBoolean'),
])


METHODS.extend([
    ('ba', 'a', '(Ljava/lang/String;IJJLjava/lang/String;)[B', 'a'),
    ('ba', 'a', '([BI)I', 'a'),
    ('ba', 'b', '([BI)I', 'b'),
    ('co', 'c', '(Ljava/lang/String;)Ljava/lang/String;', 'DeMangleXml'),
    ('co', 'd', '(Ljava/lang/String;)Ljava/lang/String;', 'MangleXml'),
])


METHODS.extend([
    ('bp', 'c', '(Ljava/lang/String;)Ljava/lang/String;', 'loadFromResource'),
    ('bp', 'd', '(Ljava/lang/String;)Ljava/lang/String;', 'removeCr'),
    ('bp', 'e', '(Ljava/lang/String;)Ljava/lang/String;', 'toUpperCase'),
    ('bp', 'a', '(C)C', 'toLowerCase'),
    ('bp', 'f', '(Ljava/lang/String;)Ljava/lang/String;', 'convertChar'),
    ('bp', 'a', '(Ljava/util/Vector;)[Ljava/lang/String;', 'vectorToArray'),
    ('bp', '<init>', '(Ljava/lang/String;Ljava/util/Vector;Ljava/util/Vector;)V', '<init>'),
    ('bp', 'a', '(Ljava/lang/String;Ljava/util/Vector;)V', 'convertorParser'),
    ('bp', 'a', '(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;', 'convert'),
    ('bp', 'a', '(Ljava/lang/String;)Ljava/lang/String;', 'detransliterate'),
    ('bp', 'b', '(Ljava/lang/String;)Ljava/lang/String;', 'transliterate'),
    ('bp', '<clinit>', '()V', '<clinit>'),
])


METHODS.extend([
    ('cs', '<init>', '()V', '<init>'),
    ('cs', 'a', '()V', 'refreshBday'),
    ('cs', 'run', '()V', 'run'),
    ('cs', 'a', '(Ljava/lang/String;II)V', 'additemB'),
    ('cs', 'a', '(Ljava/lang/String;)V', 'deleteBitem'),
    ('cs', 'a', '(Ljava/lang/String;)I', 'checkDatacurrData'),
    ('cs', 'b', '()V', 'load'),
    ('cs', 'c', '()V', 'save'),
    ('cs', '<clinit>', '()V', '<clinit>'),
    ('co', 'a', '(ZZ)J', 'createCurrentDate'),
    ('co', 'a', '(J)[I', 'createDate'),
    ('co', 'a', '(IIIIII)J', 'createLongTime'),
    ('co', 'b', '(I)I', 'convertDateMonToSimpleMon'),
    ('co', 'a', '(J)J', 'gmtTimeToLocalTime'),
])


METHODS.extend([
    ('bf', '<init>', '(Lr;)V', '<init>'),
    ('bf', 'a', '(Ljava/lang/String;)V', 'connect'),
    ('bf', 'a', '(Lan;)V', 'sendPacket'),
    ('bf', 'b', '()I', 'getLocalPort'),
    ('bf', 'a', '()[B', 'getLocalIP'),
    ('bf', 'run', '()V', 'run'),
    ('bf', 'a', '()V', 'close'),
])


METHODS.extend([
    ('ce', '<init>', '(Ljava/lang/String;Ljava/lang/String;)V', '<init>'),
    ('ce', 'a', '()V', 'init'),
    ('ce', 'a', '(Lan;)Z', 'forward'),
    ('ce', 'a', '(Ljava/lang/String;II)V', 'initInterestsDataItem'),
    ('ce', 'b', '(I)Ljava/lang/String;', 'getCategoriesString'),
    ('ce', 'a', '(I)I', 'getSelectIndex'),
    ('ce', 'b', '(I)I', 'getCategoriesCode'),
    ('ce', 'a', '(I)Ljava/lang/String;', 'getCategoriesName'),
    ('ce', 'a', '()Z', 'isCompleted'),
    ('ce', 'b', '()Z', 'isError'),
    ('ce', '<clinit>', '()V', '<clinit>'),
])


METHODS.extend([
    ('bh', '<init>', '([B)V', '<init>'),
    ('bj', '<init>', '()V', '<init>'),
    ('bj', 'a', '(I)V', 'setStatusIndex'),
    ('bj', 'a', '(I)Lbh;', 'getStatusGUID'),
    ('bj', 'a', '(I)Le;', 'getStatusImage'),
    ('bj', 'a', '(I)Ljava/lang/String;', 'getStatusAsString'),
    ('bj', 'a', '()I', 'getXStatusCount'),
    ('bj', 'b', '()I', 'getStatusIndex'),
    ('bj', 'a', '()Lf;', 'getXStatusImageList'),
    ('bj', '<clinit>', '()V', '<clinit>'),
    ('z', 'a', '([B)V', 'setXStatus'),
    ('z', 'a', '()Lbj;', 'getXStatus'),
])


METHODS.extend([
    ('aa', '<init>', '(ZZ)V', '<init>'),
    ('aa', 'a', '(Lr;)V', 'setIcq'),
    ('aa', 'c', '()Z', 'isExecutable'),
    ('aa', 'a', '()I', 'getProgress'),
    ('aa', 'a', '()Ljava/lang/String;', 'getProgressMsg'),
    ('aa', 'a', '(I)V', 'onEvent'),
    ('ac', '<init>', '(JLjava/lang/String;Ljava/lang/String;I)V', '<init>'),
    ('ac', 'a', '()Ljava/lang/String;', 'getRcvrUin'),
    ('ac', 'a', '()Lz;', 'getRcvr'),
    ('ac', 'a', '()Z', 'getOffline'),
    ('av', '<init>', '(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Z)V', '<init>'),
    ('av', '<init>', '(Ljava/lang/String;Lz;IJLjava/lang/String;)V', '<init>'),
    ('bk', '<init>', '(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;)V', '<init>'),
    ('bz', '<init>', '()V', '<init>'),
    ('r', 'b', '()Z', 'isNotConnected'),
])


METHODS.extend([
    ('by', '<init>', '(Ljavax/microedition/lcdui/Displayable;)V', '<init>'),
    ('by', 'a', '(Ljavax/microedition/lcdui/Displayable;)V', 'activate'),
    ('by', 'a', '()V', 'autoConnect'),
    ('by', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
])


CLASSES['bd'] = 'jimm/util/PhoneBook'
SYMBOLS.update({
    'bd.a Lbd;': 'jimm/util/PhoneBook.instance Ljimm/util/PhoneBook;',
    'bd.a Ljavax/microedition/lcdui/TextBox;': 'jimm/util/PhoneBook.SmsTextBox Ljavax/microedition/lcdui/TextBox;',
    'bd.b Ljavax/microedition/lcdui/TextBox;': 'jimm/util/PhoneBook.inputNumber Ljavax/microedition/lcdui/TextBox;',
    'bd.a Ljavax/microedition/lcdui/Command;': 'jimm/util/PhoneBook.cmdBack Ljavax/microedition/lcdui/Command;',
    'bd.b Ljavax/microedition/lcdui/Command;': 'jimm/util/PhoneBook.cmdSms Ljavax/microedition/lcdui/Command;',
    'bd.c Ljavax/microedition/lcdui/Command;': 'jimm/util/PhoneBook.cmdCall Ljavax/microedition/lcdui/Command;',
    'bd.d Ljavax/microedition/lcdui/Command;': 'jimm/util/PhoneBook.cmdSend Ljavax/microedition/lcdui/Command;',
})
METHODS.extend([
    ('bd', '<init>', '()V', '<init>'),
    ('bd', '<clinit>', '()V', '<clinit>'),
    ('bd', 'a', '()V', 'activate'),
    ('bd', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
])


SYMBOLS['as.a [Ljava/lang/String;'] = 'jimm/comm/SaveInfoAction.strData [Ljava/lang/String;'
METHODS.append(('as', '<init>', '([Ljava/lang/String;)V', '<init>'))
SYMBOLS.update({
    'co.a(ILjava/io/ByteArrayOutputStream;Ljava/lang/String;Z)V': 'jimm/comm/Util.writeAsciizTLV(ILjava/io/ByteArrayOutputStream;Ljava/lang/String;Z)V',
    'co.a(Ljava/io/ByteArrayOutputStream;I)V': 'jimm/comm/Util.writeByte(Ljava/io/ByteArrayOutputStream;I)V',
    'co.b(Ljava/lang/String;)I': 'jimm/comm/Util.stringToGender(Ljava/lang/String;)I',
})
METHODS.extend([
    ('co', 'a', '(ILjava/io/ByteArrayOutputStream;Ljava/lang/String;Z)V', 'writeAsciizTLV'),
    ('co', 'a', '(Ljava/io/ByteArrayOutputStream;I)V', 'writeByte'),
    ('co', 'b', '(Ljava/lang/String;)I', 'stringToGender'),
])


SYMBOLS.update({
    'ah.a Lah;': 'jimm/util/MagicEye.instance Ljimm/util/MagicEye;',
    'ah.b Lah;': 'jimm/util/MagicEye._this Ljimm/util/MagicEye;',
    'ah.a Lbi;': 'jimm/util/MagicEye.list LDrawControls/TextList;',
    'ah.a Ljava/util/Vector;': 'jimm/util/MagicEye.uins Ljava/util/Vector;',
    'ah.a I': 'jimm/util/MagicEye.counter I',
    'ah.a Ljavax/microedition/lcdui/Command;': 'jimm/util/MagicEye.cmdContactMenu Ljavax/microedition/lcdui/Command;',
    'ah.a()V': 'jimm/util/MagicEye.activate()V',
    'ah.b()V': 'jimm/util/MagicEye.removeCommands()V',
    'ah.a(Z)V': 'jimm/util/MagicEye.copyText(Z)V',
    'ah.a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V': 'jimm/util/MagicEye.registerAction(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V',
    'ah.a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V': 'jimm/util/MagicEye.addAction(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V',
    'ah.a(Ljava/lang/String;Ljava/lang/String;Z)V': 'jimm/util/MagicEye.addAction(Ljava/lang/String;Ljava/lang/String;Z)V',
    'cf.i Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdMenu Ljavax/microedition/lcdui/Command;',
    'cf.f Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdCopyText Ljavax/microedition/lcdui/Command;',
    'cf.g Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdCopyAppend Ljavax/microedition/lcdui/Command;',
    'cf.h Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdCopyAll Ljavax/microedition/lcdui/Command;',
    'cf.r Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdClearText Ljavax/microedition/lcdui/Command;',
    'cf.a Ljava/lang/Object;': 'jimm/JimmUI.lastScreen Ljava/lang/Object;',
    'cf.a()Z': 'jimm/JimmUI.clipBoardIsEmpty()Z',
    'cf.c()V': 'jimm/JimmUI.clearClipBoardText()V',
    'cf.a(Ljava/lang/Object;)Ljava/lang/String;': 'jimm/JimmUI.getCaption(Ljava/lang/Object;)Ljava/lang/String;',
    'cf.a(Z)Ljava/lang/String;': 'jimm/JimmUI.getClipBoardText(Z)Ljava/lang/String;',
    'cf.a(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V': 'jimm/JimmUI.setClipBoardText(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V',
    'cf.a(Lz;)V': 'jimm/JimmUI.showContactMenu(Ljimm/ContactItem;)V',
    'm.b(Ljava/lang/String;)Lz;': 'jimm/ContactList.createTempContact(Ljava/lang/String;)Ljimm/ContactItem;',
    'cd.k I': 'DrawControls/VirtualList.MENU_LEFT_BAR I',
    'cd.m I': 'DrawControls/VirtualList.MENU_LEFT I',
    'cd.m()V': 'DrawControls/VirtualList.removeAllCommands()V',
    'cd.j(I)V': 'DrawControls/VirtualList.setTopItem(I)V',
    'cd.a(Lbx;)V': 'DrawControls/VirtualList.setVLCommands(LDrawControls/VirtualListCommands;)V',
    'cd.c$13462e()V': 'DrawControls/VirtualList.setMode$13462e()V',
    'z.a Ljava/lang/String;': 'jimm/ContactItem.name Ljava/lang/String;',
    'co.a(ZZ)Ljava/lang/String;': 'jimm/comm/Util.getDateString(ZZ)Ljava/lang/String;',
})
METHODS.extend([
    ('ah', '<init>', '()V', '<init>'),
    ('ah', '<clinit>', '()V', '<clinit>'),
    ('ah', 'a', '()V', 'activate'),
    ('ah', 'b', '()V', 'removeCommands'),
    ('ah', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('ah', 'a', '(Z)V', 'copyText'),
    ('ah', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('ah', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('ah', 'a', '(Lcd;II)V', 'vlKeyPress'),
])


CLASSES.update({'ad': 'DrawControls/VirtualCanvas$1'})
SYMBOLS.update({
    'am.a Lcd;': 'DrawControls/VirtualCanvas.currentControl LDrawControls/VirtualList;',
    'am.a Ljava/util/Timer;': 'DrawControls/VirtualCanvas.repeatTimer Ljava/util/Timer;',
    'am.a Ljava/util/TimerTask;': 'DrawControls/VirtualCanvas.timerTask Ljava/util/TimerTask;',
    'am.a I': 'DrawControls/VirtualCanvas.lastKeyKode I',
    'am.a Ljavax/microedition/lcdui/Display;': 'DrawControls/VirtualCanvas.display Ljavax/microedition/lcdui/Display;',
    'am.a()V': 'DrawControls/VirtualCanvas.cancelKeyRepeatTask()V',
    'ad.a Lam;': 'DrawControls/VirtualCanvas$1.this$0 LDrawControls/VirtualCanvas;',
    'cd.c(II)V': 'DrawControls/VirtualList.keyReaction(II)V',
    'cd.f(I)V': 'DrawControls/VirtualList.keyPressed(I)V',
    'cd.g(I)V': 'DrawControls/VirtualList.keyRepeated(I)V',
    'cd.h(I)V': 'DrawControls/VirtualList.keyReleased(I)V',
    'cd.i(I)V': 'DrawControls/VirtualList.pointerDragged$255f295(I)V',
    'cd.b(II)V': 'DrawControls/VirtualList.pointerPressed(II)V',
    'cd.f()V': 'DrawControls/VirtualList.showNotify()V',
    'cd.a(Ljavax/microedition/lcdui/Graphics;)V': 'DrawControls/VirtualList.paint(Ljavax/microedition/lcdui/Graphics;)V',
    'aj.a()V': 'DrawControls/LightControl.reset()V',
    'co.a(Ljava/io/ByteArrayOutputStream;[B)V': 'jimm/comm/Util.writeByteArray(Ljava/io/ByteArrayOutputStream;[B)V',
})
METHODS.extend([
    ('co', 'a', '(Ljava/io/ByteArrayOutputStream;[B)V', 'writeByteArray'),
    ('cd', 'a', '(II)V', 'doKeyreaction'),
    ('cd', 'f', '(I)V', 'keyPressed'),
    ('cd', 'g', '(I)V', 'keyRepeated'),
    ('cd', 'h', '(I)V', 'keyReleased'),
    ('am', '<init>', '()V', '<init>'),
    ('am', 'paint', '(Ljavax/microedition/lcdui/Graphics;)V', 'paint'),
    ('am', 'showNotify', '()V', 'showNotify'),
    ('am', 'hideNotify', '()V', 'hideNotify'),
    ('am', 'run', '()V', 'run'),
    ('am', 'keyPressed', '(I)V', 'keyPressed'),
    ('am', 'keyReleased', '(I)V', 'keyReleased'),
    ('am', 'a', '()V', 'cancelKeyRepeatTask'),
    ('am', 'pointerDragged', '(II)V', 'pointerDragged'),
    ('am', 'pointerPressed', '(II)V', 'pointerPressed'),
    ('ad', '<init>', '(Lam;)V', '<init>'),
    ('ad', 'run', '()V', 'run'),
])


# Util has overloaded obfuscated names, including methods distinguished only by
# their return type. Keep typed identities and optimizer specializations explicit.
UTIL_SYMBOLS = json.loads((ROOT / 'tools/source/util-symbols.json').read_text(encoding='utf-8'))
for member in UTIL_SYMBOLS['fields']:
    SYMBOLS['co.' + member['reference_name'] + ' ' + member['desc']] = (
        'jimm/comm/Util.' + member['source_name'] + ' ' + member['desc'])
_audited_signatures = {(owner, name, desc) for owner, name, desc, source in METHODS}
for member in UTIL_SYMBOLS['methods']:
    name, desc = member['reference_name'], member['reference_desc']
    if member['source_optimized_name'] is not None:
        SYMBOLS['co.' + name + desc] = ('jimm/comm/Util.' + member['source_optimized_name']
                                      + member['source_optimized_desc'])
    if member['same_optimized_signature'] and ('co', name, desc) not in _audited_signatures:
        METHODS.append(('co', name, desc, member['source_optimized_name']))
        _audited_signatures.add(('co', name, desc))


# Typed contact members and the remaining animation/timer classes. The inventory
# keeps specialized and compiler-generated methods separate from same signatures.
REMAINING_SYMBOLS = json.loads((ROOT / 'tools/source/remaining-symbols.json').read_text(encoding='utf-8'))
CLASSES.update(REMAINING_SYMBOLS['classes'])
SYMBOLS.update(REMAINING_SYMBOLS['fields'])
SYMBOLS.update(REMAINING_SYMBOLS['method_symbols'])
for member in REMAINING_SYMBOLS['additional_methods']:
    signature = (member['reference_owner'], member['reference_name'], member['reference_desc'])
    assert signature not in _audited_signatures, signature
    assert member['source_owner'] == SOURCE_OWNERS.get(signature[0], CLASSES[signature[0]])
    METHODS.append((*signature, member['source_name']))
    _audited_signatures.add(signature)
    SYMBOLS[signature[0] + '.' + signature[1] + signature[2]] = (
        member['source_owner'] + '.' + member['source_name'] + member['source_desc'])


# Complete typed call names from previously verified method declarations, and
# abstract members identified by the actual override hierarchy. Equal constant
# bodies in GroupItem cannot establish which ContactListItem method is overridden.
INHERITED_SYMBOLS = json.loads((ROOT / 'tools/source/inherited-symbols.json').read_text(encoding='utf-8'))
SYMBOLS.update(INHERITED_SYMBOLS['method_symbols'])
for member in INHERITED_SYMBOLS['abstract_methods']:
    signature = (member['reference_owner'], member['reference_name'], member['reference_desc'])
    if member['same_optimized_signature'] and signature not in _audited_signatures:
        assert member['source_owner'] == CLASSES[signature[0]]
        METHODS.append((*signature, member['source_name']))
        _audited_signatures.add(signature)


# Further identities proven by unique full-body matches. The separate audit
# replays their dependency rounds without using these aliases prematurely.
EXACT_BASE_SYMBOLS = dict(SYMBOLS)
EXACT_METHODS = json.loads((ROOT / 'tools/source/exact-methods.json').read_text(encoding='utf-8'))
for member in EXACT_METHODS['methods']:
    signature = (member['reference_owner'], member['reference_name'], member['reference_desc'])
    assert signature not in _audited_signatures, signature
    assert member['source_owner'] == SOURCE_OWNERS.get(signature[0], CLASSES[signature[0]])
    METHODS.append((*signature, member['source_name']))
    _audited_signatures.add(signature)
    SYMBOLS[signature[0] + '.' + signature[1] + signature[2]] = (
        member['source_owner'] + '.' + member['source_name'] + member['source_desc'])


# Public URL/sender/typing entry points, exercised by the corresponding runtime
# probes. The typing declaration must retain its static synchronized contract.
assert ('cf', 'a', '(Ljava/lang/String;Ljava/lang/Object;)V') not in _audited_signatures
METHODS.append(('cf', 'a', '(Ljava/lang/String;Ljava/lang/Object;)V', 'gotoURL'))
METHODS.append(('cf', 'a', '(Ljava/lang/String;Lz;)V', 'sendMessage'))
METHODS.append(('r', 'a', '(Ljava/lang/String;Z)V', 'beginTyping'))

# Editor identities are anchored by actual creation/pagination execution and field access roles.
# Keep them after EXACT_BASE_SYMBOLS so the historical dependency-graph proof remains independent.
METHODS.extend([
    ('cf', 'a', '(Ljava/lang/String;)V', 'setCaption'),
    ('cf', 'h', '()V', 'saveCurPage'),
    ('cf', 'i', '()V', 'setCurrentScreen'),
    ('cf', 'c', '()Ljava/lang/String;', 'getString'),
    ('cf', 'a', '(Ljava/lang/String;I)V', 'insert'),
    ('cf', 'a', '(Lz;Ljava/lang/String;)V', 'writeMessage'),
])
SYMBOLS.update({
    'cf.a Ljavax/microedition/lcdui/TextBox;': 'jimm/JimmUI.messageTextbox Ljavax/microedition/lcdui/TextBox;',
    'cf.a Ljava/util/Vector;': 'jimm/JimmUI.strings Ljava/util/Vector;',
    'cf.d I': 'jimm/JimmUI.current I',
    'cf.e I': 'jimm/JimmUI.textLimit I',
    'cf.h Ljava/lang/String;': 'jimm/JimmUI.caption Ljava/lang/String;',
    'cf.a(Ljava/lang/String;)V': 'jimm/JimmUI.setCaption(Ljava/lang/String;)V',
    'cf.h()V': 'jimm/JimmUI.saveCurPage()V',
    'cf.i()V': 'jimm/JimmUI.setCurrentScreen()V',
    'cf.c()Ljava/lang/String;': 'jimm/JimmUI.getString()Ljava/lang/String;',
    'cf.a(Ljava/lang/String;I)V': 'jimm/JimmUI.insert(Ljava/lang/String;I)V',
    'cf.a(Lz;Ljava/lang/String;)V': 'jimm/JimmUI.writeMessage(Ljimm/ContactItem;Ljava/lang/String;)V',
})

# Genuine three-platform hotkey execution establishes these dispatcher/facade roles.
# The MIDP2 light toggle keeps changeState/On in the optimized application again.
METHODS.extend([
    ('cf', 'a', '(ILz;I)V', 'execHotKeyAction'),
    ('cf', 'a', '(Lz;II)V', 'execHotKey'),
    ('cf', 'a', '(Lz;II)Z', 'execDoubleHotKey'),
    ('aj', 'b', '()V', 'changeState'),
    ('aj', 'e', '()V', 'On'),
])
SYMBOLS.update({
    'cf.a J': 'jimm/JimmUI.lockPressedTime J',
    'cd.a Z': 'DrawControls/VirtualList.zeroWasPressed Z',
    'cj.a I': 'jimm/Options.EXT_KEY_COUNT I',
    'cj.a [I': 'jimm/Options.EXT_KEY_CODES [I',
    'cj.d()V': 'jimm/Options.safe_save()V',
    'cj.e()V': 'jimm/Options.editOptions()V',
    'cf.a(ILz;I)V': 'jimm/JimmUI.execHotKeyAction(ILjimm/ContactItem;I)V',
    'cf.a(Lz;II)V': 'jimm/JimmUI.execHotKey(Ljimm/ContactItem;II)V',
    'cf.a(Lz;II)Z': 'jimm/JimmUI.execDoubleHotKey(Ljimm/ContactItem;II)Z',
    'aj.b()V': 'DrawControls/LightControl.changeState()V',
    'aj.e()V': 'DrawControls/LightControl.On()V',
})


# Complete controller declarations, including compiler accessors with incompatible
# return types recorded separately. Introduce these names after the frozen graph base.
UI_SYMBOLS = json.loads((ROOT / 'tools/source/ui-symbols.json').read_text(encoding='utf-8'))
for _key, _value in {**UI_SYMBOLS['fields'], **UI_SYMBOLS['method_symbols']}.items():
    assert _key not in SYMBOLS or SYMBOLS[_key] == _value, (_key, SYMBOLS.get(_key), _value)
    SYMBOLS[_key] = _value
_ui_audited = {(owner, name, desc) for owner, name, desc, source in METHODS}
for _member in UI_SYMBOLS['methods']:
    _identity = ('cf', _member['reference_name'], _member['reference_desc'])
    if _member['same_optimized_signature'] and _identity not in _ui_audited:
        METHODS.append((*_identity, _member['source_optimized_name']))
        _ui_audited.add(_identity)


# May device key mapping retains raw unknown codes; authored instance method is made static by ProGuard.
METHODS.append(('cd', 'b', '(I)I', 'getExtendedGameAction'))
SYMBOLS['cd.b(I)I'] = 'DrawControls/VirtualList.getExtendedGameAction(I)I'

# Native cache guard, construction and real three-platform menu reuse establish this entry.
METHODS.append(('cj', 'e', '()V', 'editOptions'))
SYMBOLS.update({
    'cj.a Lcg;': 'jimm/Options.optionsForm Ljimm/OptionsForm;',
    'cg.c()V': 'jimm/OptionsForm.activate()V',
})

# Actual settings tables/device branches and complete RMS load replay establish these roles.
# Keep this after the frozen graph inventory so previous independent proofs retain their inputs.
METHODS.extend([
    ('cj', 'a', '()V', 'updateAlpha'),
    ('cj', 'b', '()V', 'updateFontStyle'),
    ('cj', 'f', '()V', 'setDefaults'),
    ('m', 'h', '()V', 'load'),
])
SYMBOLS.update({
    'cj.a()V': 'jimm/Options.updateAlpha()V',
    'cj.b()V': 'jimm/Options.updateFontStyle()V',
    'cj.f()V': 'jimm/Options.setDefaults()V',
    'cj.a Ljava/lang/String;': 'jimm/Options.emptyString Ljava/lang/String;',
    'cd.j I': 'DrawControls/VirtualList.gradientHeight I',
    'm.h()V': 'jimm/ContactList.load()V',
    'jimm/Jimm.a Ljava/lang/String;': 'jimm/Jimm.VERSION Ljava/lang/String;',
    'jimm/Jimm.e Z': 'jimm/Jimm.is_phone_NOKIA Z',
})

# Whole three-platform MMAPI executions establish these private helpers and the player field.
# Preserve the frozen dependency-graph inventory above; no compiler differences are removed.
METHODS.extend([
    ('m', 'a', '(Ljava/lang/String;)Z', 'testSoundFile'),
    ('m', 'a', '(Ljava/lang/String;)Ljavax/microedition/media/Player;', 'createPlayer'),
    ('m', 'k', '()V', 'closePlayer'),
    ('m', 'c', '(I)V', 'setVolume'),
    ('cj', 'a', '(Ljava/lang/String;I)V', 'selectSoundType'),
])
SYMBOLS.update({
    'm.a(Ljava/lang/String;)Z': 'jimm/ContactList.testSoundFile(Ljava/lang/String;)Z',
    'm.a(Ljava/lang/String;)Ljavax/microedition/media/Player;': 'jimm/ContactList.createPlayer(Ljava/lang/String;)Ljavax/microedition/media/Player;',
    'm.k()V': 'jimm/ContactList.closePlayer()V',
    'm.c(I)V': 'jimm/ContactList.setVolume(I)V',
    'cj.a(Ljava/lang/String;I)V': 'jimm/Options.selectSoundType(Ljava/lang/String;I)V',
    'm.a Ljavax/microedition/media/Player;': 'jimm/ContactList.player Ljavax/microedition/media/Player;',
})


# Roster callback and public page helper verified against May native calls and execution.
# The page helper's readable name is inferred; native bytecode retains only c(Z)V.
METHODS.extend([
    ('m', 'a', '(Lcd;II)V', 'vlKeyPress'),
    ('cd', 'c', '(Z)V', 'moveCursorByPage'),
    ('m', 'a', '(Lcd;)V', 'vlCursorMoved'),
    ('m', 'b', '(Lcd;)V', 'vlItemClicked'),
    ('m', 'a', '()Lf;', 'getImageList'),
])
SYMBOLS.update({
    'm.a(Lcd;II)V': 'jimm/ContactList.vlKeyPress(LDrawControls/VirtualList;II)V',
    'cd.c(Z)V': 'DrawControls/VirtualList.moveCursorByPage(Z)V',
    'm.a(Lcd;)V': 'jimm/ContactList.vlCursorMoved(LDrawControls/VirtualList;)V',
    'm.b(Lcd;)V': 'jimm/ContactList.vlItemClicked(LDrawControls/VirtualList;)V',
    'm.a()Lf;': 'jimm/ContactList.getImageList()LDrawControls/ImageList;',
    'm.a Lf;': 'jimm/ContactList.imageList LDrawControls/ImageList;',
})


# Remaining ContactList state methods execute with real chat activation and RMS persistence.
# Their inlined contact/chat helpers are recorded separately in audit_roster_state.py.
METHODS.extend([
    ('m', 'a', '(Z)Ljava/lang/String;', 'showNextPrevChat'),
    ('m', 'd', '()I', 'getUnreadMessCount'),
    ('m', 'a', '(Lbb;)[Lz;', 'getItems'),
    ('m', 'b', '(Ljava/lang/String;Z)V', 'TypingHelper'),
    ('m', 'a', '(Z)Z', 'changeSoundMode'),
    ('cj', 'd', '()V', 'safe_save'),
])
SYMBOLS.update({
    'm.a(Z)Ljava/lang/String;': 'jimm/ContactList.showNextPrevChat(Z)Ljava/lang/String;',
    'm.d()I': 'jimm/ContactList.getUnreadMessCount()I',
    'm.a(Lbb;)[Lz;': 'jimm/ContactList.getItems(Ljimm/GroupItem;)[Ljimm/ContactItem;',
    'm.b(Ljava/lang/String;Z)V': 'jimm/ContactList.TypingHelper(Ljava/lang/String;Z)V',
    'm.a(Z)Z': 'jimm/ContactList.changeSoundMode(Z)Z',
    'm.a Lz;': 'jimm/ContactList.lastChatItem Ljimm/ContactItem;',
    'cf.a Lz;': 'jimm/JimmUI.textMessReceiver Ljimm/ContactItem;',
    'z.e()I': 'jimm/ContactItem.getUnreadMessCount()I',
})


# Remaining Icq entry points retain inherited source names. The unused private-ID
# getter is absent from the modern optimized JAR and is recorded in audit_icq_state.
METHODS.extend([
    ('r', 'a', '(Laa;)V', 'requestAction'),
    ('r', 'a', '(Lz;)V', 'addToContactList'),
    ('r', 'a', '(Lz;)Z', 'delFromContactList'),
    ('r', 'c', '()V', 'setNotConnected'),
    ('r', 'd', '()V', 'setConnected'),
    ('r', 'd', '()I', 'setWebAware'),
    ('r', 'g', '()V', 'setPoint'),
    ('r', 'a', '()Ljava/lang/String;', 'getLastStatusChangeTime'),
    ('r', 'a', '(B)V', 'setPrivateStatus'),
    ('r', 'c', '(I)V', 'setPrivateStatusId'),
])
SYMBOLS.update({
    'r.a(Laa;)V': 'jimm/comm/Icq.requestAction(Ljimm/comm/Action;)V',
    'r.a(Lz;)V': 'jimm/comm/Icq.addToContactList(Ljimm/ContactItem;)V',
    'r.a(Lz;)Z': 'jimm/comm/Icq.delFromContactList(Ljimm/ContactItem;)Z',
    'r.c()V': 'jimm/comm/Icq.setNotConnected()V',
    'r.d()V': 'jimm/comm/Icq.setConnected()V',
    'r.d()I': 'jimm/comm/Icq.setWebAware()I',
    'r.g()V': 'jimm/comm/Icq.setPoint()V',
    'r.a()Ljava/lang/String;': 'jimm/comm/Icq.getLastStatusChangeTime()Ljava/lang/String;',
    'r.a(B)V': 'jimm/comm/Icq.setPrivateStatus(B)V',
    'r.c(I)V': 'jimm/comm/Icq.setPrivateStatusId(I)V',
    'r.e()I': 'jimm/comm/Icq.getPrivateStatusId()I',
    'r.a Ljava/util/Vector;': 'jimm/comm/Icq.reqAction Ljava/util/Vector;',
    'r.a Ljava/lang/Thread;': 'jimm/comm/Icq.thread Ljava/lang/Thread;',
    'r.a Ljava/lang/Object;': 'jimm/comm/Icq.wait Ljava/lang/Object;',
    'r.b Z': 'jimm/comm/Icq.connected Z',
    'r.b Ljava/lang/String;': 'jimm/comm/Icq.lastStatusChangeTime Ljava/lang/String;',
    'r.c I': 'jimm/comm/Icq.privateId I',
    'bv.a Z': 'jimm/JimmException.critical Z',
    'bq.a(B)V': 'jimm/comm/OtherAction.setPrivateStatus(B)V',
})


# Whole capability/status/private packet methods are replayed through real socket
# serialization in test_other_status.py; only setStatus has a fully equal body.
METHODS.extend([
    ('bq', 'a', '([Lbh;)V', 'setUserInfo'),
    ('bq', 'a', '(I)V', 'setStatus'),
    ('bq', 'a', '(B)V', 'setPrivateStatus'),
])
SYMBOLS.update({
    'bq.a([Lbh;)V': 'jimm/comm/OtherAction.setUserInfo([Ljimm/comm/GUID;)V',
    'bq.a(I)V': 'jimm/comm/OtherAction.setStatus(I)V',
    'bq.a [B': 'jimm/comm/OtherAction.CLI_SETSTATUS_DATA [B',
    'bq.a Z': 'jimm/comm/OtherAction.extendedStatusSent Z',
})


def normalized(code):
    result = []
    for instruction in code:
        # A short obfuscated owner (e.g. o) must not match the suffix of co.
        opcode, separator, operand = instruction.partition(' ')
        if operand in SYMBOLS:
            instruction = opcode + separator + SYMBOLS[operand]
        elif opcode in ('187', '189', '192', '193') and operand in CLASSES:
            instruction = opcode + separator + CLASSES[operand]
        elif opcode == '183' and '.<init>(' in operand:
            owner, constructor = operand.split('.', 1)
            if owner in CLASSES:
                instruction = opcode + separator + CLASSES[owner] + '.' + constructor
        for before, after in CLASSES.items():
            instruction = instruction.replace('L' + before + ';', 'L' + after + ';')
        result.append(instruction)
    return result


def digest(code):
    return hashlib.sha256('\n'.join(code).encode()).hexdigest()


def normalized_handlers(handlers):
    result = []
    for entry in handlers:
        region, _, exception = entry.rpartition(' ')
        result.append(region + ' ' + CLASSES.get(exception, exception))
    return result


def resolve_method(methods, name, desc):
    exact = [m for m in methods if (m['name'], m['desc']) == (name, desc)]
    if exact:
        assert len(exact) == 1, ('Duplicate exact member', name, desc)
        return exact[0]
    specialized = [m for m in methods if '$' not in name and m['name'].split('$')[0] == name
                   and m['desc'] == desc]
    assert len(specialized) == 1, ('Missing or ambiguous specialized member', name, desc,
                                  [m['name'] for m in specialized])
    return specialized[0]


def main():
    signatures = [(owner, name, desc) for owner, name, desc, source in METHODS]
    if len(signatures) != len(set(signatures)):
        raise AssertionError('Duplicate reference method in audit inventory')
    recover.bootstrap()
    OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', OUT,
                 ROOT / 'tools/recovery/BytecodeDump.java'])
    def dump(path):
        data = json.loads(recover.run([recover.java(), '-cp', recover.cp([OUT, cp]),
                                      'BytecodeDump', path], capture=True))
        return {c['name']: c for c in data}
    reference = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    rebuilt = ROOT / 'build/source/MIDP2-RU/preverified.jar'
    old, new = dump(reference), dump(rebuilt)
    methods = []
    for owner, name, desc, source_name in METHODS:
        before = next(m for m in old[owner]['methods'] if (m['name'], m['desc']) == (name, desc))
        source_desc = desc
        for short, long in CLASSES.items():
            source_desc = source_desc.replace('L' + short + ';', 'L' + long + ';')
        source_owner = SOURCE_OWNERS.get(owner, CLASSES[owner])
        after = resolve_method(new[source_owner]['methods'], source_name, source_desc)
        left, right = normalized(before['code']), after['code']
        same_static = bool(before['access'] & 8) == bool(after['access'] & 8)
        if not same_static:
            raise AssertionError('Static/instance mismatch: ' + owner + '.' + name + desc)
        same_synchronized = bool(before['access'] & 32) == bool(after['access'] & 32)
        if not same_synchronized:
            raise AssertionError('Synchronization mismatch: ' + owner + '.' + name + desc)
        same_abstract = bool(before['access'] & 1024) == bool(after['access'] & 1024)
        if not same_abstract:
            raise AssertionError('Abstract/concrete mismatch: ' + owner + '.' + name + desc)
        same_handlers = normalized_handlers(before['handlers']) == after['handlers']
        methods.append({'reference': owner + '.' + name + desc,
                        'source': source_owner + '.' + after['name'] + source_desc,
                        'signature_verified': True, 'static_modifier_verified': same_static,
                        'synchronized_modifier_verified': same_synchronized,
                        'abstract_modifier_verified': same_abstract,
                        'abstract_method': bool(before['access'] & 1024),
                        'reference_instructions': len(left),
                        'source_instructions': len(right), 'same_normalized_instructions': left == right,
                        'same_normalized_handlers': same_handlers,
                        'same_normalized_bytecode': left == right and same_handlers,
                        'reference_normalized_sha256': digest(left), 'source_normalized_sha256': digest(right)})
    report = {'reference_sha256': recover.sha(reference), 'rebuilt_sha256': recover.sha(rebuilt),
              'reference_classes': len(old), 'rebuilt_classes': len(new), 'methods': methods,
              'abstract_declarations': sum(m['abstract_method'] for m in methods),
              'same_normalized_executable_method_bodies': sum(m['same_normalized_bytecode'] and not m['abstract_method'] for m in methods),
              'scope': 'Verified subset only. Instruction equality includes local slots and branch layout; '
                       'compiler and optimizer differences remain. Functional checks are recorded separately.'}
    path = ROOT / 'preservation/reports/source-bytecode-comparison.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8')
    print('Audited', len(methods), 'method signatures;', sum(m['same_normalized_instructions'] for m in methods),
          'identical normalized instruction sequences;', sum(m['same_normalized_bytecode'] for m in methods),
          'also match exception tables')


if __name__ == '__main__':
    main()
