import JsBridge from './NativeJsBridge';

export function multiply(a: number, b: number): number {
  return JsBridge.multiply(a, b);
}
