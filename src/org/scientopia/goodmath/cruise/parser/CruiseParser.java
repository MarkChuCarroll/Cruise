/* Generated By:JavaCC: Do not edit this line. CruiseParser.java */
package org.scientopia.goodmath.cruise.parser;
import org.scientopia.goodmath.cruise.interp.*;
import org.scientopia.goodmath.cruise.ast.*;
import java.util.*;

public class CruiseParser implements CruiseParserConstants {
  public static void main(String args[])throws ParseException{
    CruiseParser parser = new CruiseParser(System.in);
    while (true){
      System.out.println("Reading from standard input...");
      try {
                CruiseParser.program();
      }
      catch (Exception e){
        System.out.println("NOK.");
        System.out.println(e.getMessage());
      }
      catch (Error e){
        System.out.println("Oops.");
        System.out.println(e.getMessage());
        break ;
      }
    }
  }

  static final public Engine program() throws ParseException {
                    Engine engine = new Engine(); ActorType at; CreateActorStatement createStmt; SendMessageStatement sendStmt;
    label_1:
    while (true) {
      at = actor_type();
                       engine.addActorType(at);
      if (jj_2_1(4)) {
        ;
      } else {
        break label_1;
      }
    }
    label_2:
    while (true) {
      createStmt = create_actor_stmt();
                                      engine.getCreates().add(createStmt);
      if (jj_2_2(4)) {
        ;
      } else {
        break label_2;
      }
    }
    label_3:
    while (true) {
      sendStmt = msg_send_stmt();
                                  engine.addMessage(sendStmt);
      if (jj_2_3(4)) {
        ;
      } else {
        break label_3;
      }
    }
     {if (true) return engine;}
    throw new Error("Missing return statement in function");
  }

  static final public ActorType actor_type() throws ParseException {
                           Token typeToken = null;
                                          ActorType result;
                                          Token bname;
                                          Behavior beh;
    jj_consume_token(ACTOR);
    typeToken = jj_consume_token(TYPEIDENT);
                                                          result = new ActorType(typeToken.image);
    jj_consume_token(LBRACE);
    label_4:
    while (true) {
      beh = behavior();
                               result.addBehavior(beh);
      if (jj_2_4(4)) {
        ;
      } else {
        break label_4;
      }
    }
    jj_consume_token(INIT);
    bname = jj_consume_token(BEHAVIORIDENT);
                                   Behavior init = result.getBehavior(bname.image);
                                                               if (init == null) {
                                                                  {if (true) throw new ParseException("Initial behavior " + bname.image + " not defined  for actor type " + typeToken.image );}
                                                       }
                                                       result.setInitialBehavior(bname.image);
    jj_consume_token(RBRACE);
     {if (true) return result;}
    throw new Error("Missing return statement in function");
  }

  static final public void var_ident_list(List<String> names) throws ParseException {
                                           Token tok;
    tok = jj_consume_token(VARIABLEIDENT);
                         names.add(tok.image);
    label_5:
    while (true) {
      if (jj_2_5(4)) {
        ;
      } else {
        break label_5;
      }
      jj_consume_token(COMMA);
      tok = jj_consume_token(VARIABLEIDENT);
                                    names.add(tok.image);
    }
  }

  static final public Behavior behavior() throws ParseException {
                        Behavior result; Token name; MessageHandler handler;
    jj_consume_token(BEHAVIOR);
    name = jj_consume_token(BEHAVIORIDENT);
                                    result = new Behavior(name.image);
    jj_consume_token(LPAREN);
    if (jj_2_6(4)) {
      var_ident_list(result.getParameters());
    } else {
      ;
    }
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    label_6:
    while (true) {
      handler = msg_handler_clause();
                                            result.getHandlers().add(handler);
      if (jj_2_7(4)) {
        ;
      } else {
        break label_6;
      }
    }
    jj_consume_token(RBRACE);
    {if (true) return result;}
    throw new Error("Missing return statement in function");
  }

  static final public MessageHandler msg_handler_clause() throws ParseException {
                                       MessageHandler result; CreateActorStatement createStmt; SendMessageStatement sendStmt; ValueExpression pattern;
    jj_consume_token(ON);
    pattern = value_spec();
                                     result = new MessageHandler(pattern);
    jj_consume_token(LBRACE);
    label_7:
    while (true) {
      if (jj_2_8(4)) {
        ;
      } else {
        break label_7;
      }
      createStmt = create_actor_stmt();
                                                   result.getCreations().add(createStmt);
    }
    label_8:
    while (true) {
      if (jj_2_9(4)) {
        ;
      } else {
        break label_8;
      }
      sendStmt = msg_send_stmt();
                                      result.getSends().add(sendStmt);
    }
    if (jj_2_10(4)) {
      adopt_clause(result);
    } else {
      ;
    }
    jj_consume_token(RBRACE);
          {if (true) return result;}
    throw new Error("Missing return statement in function");
  }

  static final public ValueExpression value_spec() throws ParseException {
                                Token tok; TupleExpression tup; VariableExpression var; ActorIdentifier actId;
    if (jj_2_12(4)) {
      tok = jj_consume_token(TUPLETAG);
                       tup = new TupleExpression(tok.image);
      jj_consume_token(LPAREN);
      if (jj_2_11(4)) {
        value_spec_list(tup.getElements());
      } else {
        ;
      }
      jj_consume_token(RPAREN);
                                                                                                                              {if (true) return tup;}
    } else if (jj_2_13(4)) {
      tok = jj_consume_token(VARIABLEIDENT);
                                {if (true) return new VariableExpression(tok.image);}
    } else if (jj_2_14(4)) {
      tok = jj_consume_token(ACTORIDENT);
                             {if (true) return new ActorIdentifier(tok.image);}
    } else if (jj_2_15(4)) {
      tok = jj_consume_token(NUMBER);
                         {if (true) return ValueExpression.intToTuple(Integer.parseInt(tok.image));}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public void value_spec_list(List<ValueExpression> values) throws ParseException {
                                                       ValueExpression v;
    v = value_spec();
                      values.add(v);
    label_9:
    while (true) {
      if (jj_2_16(4)) {
        ;
      } else {
        break label_9;
      }
      jj_consume_token(COMMA);
      v = value_spec();
                                   values.add(v);
    }
  }

  static final public SendMessageStatement msg_send_stmt() throws ParseException {
                                         Token ident; ValueExpression msg; ValueExpression target;
    jj_consume_token(SEND);
    msg = value_spec();
    jj_consume_token(TO);
    if (jj_2_17(4)) {
      ident = jj_consume_token(VARIABLEIDENT);
                                  target=new VariableExpression(ident.image);
    } else if (jj_2_18(4)) {
      ident = jj_consume_token(ACTORIDENT);
                                  target=new ActorIdentifier(ident.image);
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
          {if (true) return new SendMessageStatement(target, msg);}
    throw new Error("Missing return statement in function");
  }

  static final public CreateActorStatement create_actor_stmt() throws ParseException {
                                             CreateActorStatement result; Token typeTok; Token nameTok;
    jj_consume_token(INSTANTIATE);
    typeTok = jj_consume_token(TYPEIDENT);
                                        result = new CreateActorStatement(typeTok.image);
    jj_consume_token(LPAREN);
    if (jj_2_19(4)) {
      value_spec_list(result.getParameters());
    } else {
      ;
    }
    jj_consume_token(RPAREN);
    if (jj_2_20(4)) {
      jj_consume_token(AS);
      nameTok = jj_consume_token(ACTORIDENT);
                                            result.setTarget(new ActorIdentifier(nameTok.image));
    } else if (jj_2_21(4)) {
      jj_consume_token(TO);
      nameTok = jj_consume_token(VARIABLEIDENT);
                                                    result.setTarget(new VariableExpression(nameTok.image));
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
              {if (true) return result;}
    throw new Error("Missing return statement in function");
  }

  static final public void adopt_clause(MessageHandler handler) throws ParseException {
                                             Token ident;
    jj_consume_token(ADOPT);
    ident = jj_consume_token(BEHAVIORIDENT);
                                           handler.setReplacementBehavior(ident.image);
    jj_consume_token(LPAREN);
    if (jj_2_22(4)) {
      value_spec_list(handler.getReplacementParameters());
    } else {
      ;
    }
    jj_consume_token(RPAREN);
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  static private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  static private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  static private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  static private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  static private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  static private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(10, xla); }
  }

  static private boolean jj_2_12(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_12(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(11, xla); }
  }

  static private boolean jj_2_13(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_13(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(12, xla); }
  }

  static private boolean jj_2_14(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_14(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(13, xla); }
  }

  static private boolean jj_2_15(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_15(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(14, xla); }
  }

  static private boolean jj_2_16(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_16(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(15, xla); }
  }

  static private boolean jj_2_17(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_17(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(16, xla); }
  }

  static private boolean jj_2_18(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_18(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(17, xla); }
  }

  static private boolean jj_2_19(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_19(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(18, xla); }
  }

  static private boolean jj_2_20(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_20(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(19, xla); }
  }

  static private boolean jj_2_21(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_21(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(20, xla); }
  }

  static private boolean jj_2_22(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_22(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(21, xla); }
  }

  static private boolean jj_3R_13() {
    if (jj_scan_token(BEHAVIOR)) return true;
    if (jj_scan_token(BEHAVIORIDENT)) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_6()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  static private boolean jj_3_19() {
    if (jj_3R_17()) return true;
    return false;
  }

  static private boolean jj_3_5() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(VARIABLEIDENT)) return true;
    return false;
  }

  static private boolean jj_3R_14() {
    if (jj_scan_token(VARIABLEIDENT)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_5()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3_11() {
    if (jj_3R_17()) return true;
    return false;
  }

  static private boolean jj_3_4() {
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3_18() {
    if (jj_scan_token(ACTORIDENT)) return true;
    return false;
  }

  static private boolean jj_3_17() {
    if (jj_scan_token(VARIABLEIDENT)) return true;
    return false;
  }

  static private boolean jj_3R_11() {
    if (jj_scan_token(INSTANTIATE)) return true;
    if (jj_scan_token(TYPEIDENT)) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_19()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  static private boolean jj_3R_12() {
    if (jj_scan_token(SEND)) return true;
    if (jj_3R_18()) return true;
    if (jj_scan_token(TO)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_17()) {
    jj_scanpos = xsp;
    if (jj_3_18()) return true;
    }
    return false;
  }

  static private boolean jj_3_16() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_18()) return true;
    return false;
  }

  static private boolean jj_3R_10() {
    if (jj_scan_token(ACTOR)) return true;
    if (jj_scan_token(TYPEIDENT)) return true;
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    if (jj_3_4()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_4()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3_15() {
    if (jj_scan_token(NUMBER)) return true;
    return false;
  }

  static private boolean jj_3R_17() {
    if (jj_3R_18()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_16()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3_14() {
    if (jj_scan_token(ACTORIDENT)) return true;
    return false;
  }

  static private boolean jj_3_8() {
    if (jj_3R_11()) return true;
    return false;
  }

  static private boolean jj_3_13() {
    if (jj_scan_token(VARIABLEIDENT)) return true;
    return false;
  }

  static private boolean jj_3_3() {
    if (jj_3R_12()) return true;
    return false;
  }

  static private boolean jj_3_2() {
    if (jj_3R_11()) return true;
    return false;
  }

  static private boolean jj_3_1() {
    if (jj_3R_10()) return true;
    return false;
  }

  static private boolean jj_3R_18() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_12()) {
    jj_scanpos = xsp;
    if (jj_3_13()) {
    jj_scanpos = xsp;
    if (jj_3_14()) {
    jj_scanpos = xsp;
    if (jj_3_15()) return true;
    }
    }
    }
    return false;
  }

  static private boolean jj_3_12() {
    if (jj_scan_token(TUPLETAG)) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_11()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  static private boolean jj_3_10() {
    if (jj_3R_16()) return true;
    return false;
  }

  static private boolean jj_3_9() {
    if (jj_3R_12()) return true;
    return false;
  }

  static private boolean jj_3_22() {
    if (jj_3R_17()) return true;
    return false;
  }

  static private boolean jj_3R_15() {
    if (jj_scan_token(ON)) return true;
    if (jj_3R_18()) return true;
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_8()) { jj_scanpos = xsp; break; }
    }
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_9()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_3_10()) jj_scanpos = xsp;
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  static private boolean jj_3_7() {
    if (jj_3R_15()) return true;
    return false;
  }

  static private boolean jj_3_6() {
    if (jj_3R_14()) return true;
    return false;
  }

  static private boolean jj_3_21() {
    if (jj_scan_token(TO)) return true;
    if (jj_scan_token(VARIABLEIDENT)) return true;
    return false;
  }

  static private boolean jj_3R_16() {
    if (jj_scan_token(ADOPT)) return true;
    if (jj_scan_token(BEHAVIORIDENT)) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_22()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  static private boolean jj_3_20() {
    if (jj_scan_token(AS)) return true;
    if (jj_scan_token(ACTORIDENT)) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public CruiseParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[0];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[22];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public CruiseParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public CruiseParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new CruiseParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public CruiseParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new CruiseParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public CruiseParser(CruiseParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(CruiseParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[28];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 0; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 28; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 22; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
            case 10: jj_3_11(); break;
            case 11: jj_3_12(); break;
            case 12: jj_3_13(); break;
            case 13: jj_3_14(); break;
            case 14: jj_3_15(); break;
            case 15: jj_3_16(); break;
            case 16: jj_3_17(); break;
            case 17: jj_3_18(); break;
            case 18: jj_3_19(); break;
            case 19: jj_3_20(); break;
            case 20: jj_3_21(); break;
            case 21: jj_3_22(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}