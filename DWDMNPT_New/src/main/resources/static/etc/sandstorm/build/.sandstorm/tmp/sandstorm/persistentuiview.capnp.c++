// Generated by Cap'n Proto compiler, DO NOT EDIT
// source: persistentuiview.capnp

#include "persistentuiview.capnp.h"

namespace capnp {
namespace schemas {
static const ::capnp::_::AlignedData<26> b_826f7187e23c37c9 = {
  {   0,   0,   0,   0,   5,   0,   6,   0,
    201,  55,  60, 226, 135, 113, 111, 130,
     33,   0,   0,   0,   3,   0,   0,   0,
     33,  79,  40, 153, 222, 124,  92, 254,
      0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,
     21,   0,   0,   0, 146,   1,   0,   0,
     45,   0,   0,   0,   7,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,
     41,   0,   0,   0,   7,   0,   0,   0,
     41,   0,   0,   0,  39,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,
    115,  97, 110, 100, 115, 116, 111, 114,
    109,  47, 112, 101, 114, 115, 105, 115,
    116, 101, 110, 116, 117, 105, 118, 105,
    101, 119,  46,  99,  97, 112, 110, 112,
     58,  80, 101, 114, 115, 105, 115, 116,
    101, 110, 116,  85, 105,  86, 105, 101,
    119,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   1,   0,   1,   0,
      0,   0,   0,   0,   3,   0,   5,   0,
      8,   0,   0,   0,   1,   0,   1,   0,
    231, 226, 103, 234, 152, 215, 180, 219,
      0,   0,   0,   0,   0,   0,   0,   0,
    180, 213, 190, 124, 215, 237, 140, 195,
      0,   0,   0,   0,   0,   0,   0,   0, }
};
::capnp::word const* const bp_826f7187e23c37c9 = b_826f7187e23c37c9.words;
#if !CAPNP_LITE
static const ::capnp::_::RawSchema* const d_826f7187e23c37c9[] = {
  &s_c38cedd77cbed5b4,
  &s_dbb4d798ea67e2e7,
};
const ::capnp::_::RawSchema s_826f7187e23c37c9 = {
  0x826f7187e23c37c9, b_826f7187e23c37c9.words, 26, d_826f7187e23c37c9, nullptr,
  2, 0, nullptr, nullptr, nullptr, { &s_826f7187e23c37c9, nullptr, nullptr, 0, 0, nullptr }
};
#endif  // !CAPNP_LITE
}  // namespace schemas
}  // namespace capnp

// =======================================================================================

namespace sandstorm {

#if !CAPNP_LITE
::kj::Promise<void> PersistentUiView::Server::dispatchCall(
    uint64_t interfaceId, uint16_t methodId,
    ::capnp::CallContext< ::capnp::AnyPointer, ::capnp::AnyPointer> context) {
  switch (interfaceId) {
    case 0x826f7187e23c37c9ull:
      return dispatchCallInternal(methodId, context);
    case 0xc38cedd77cbed5b4ull:
      return  ::sandstorm::SystemPersistent::Server::dispatchCallInternal(methodId, context);
    case 0xc8cb212fcd9f5691ull:
      return  ::capnp::Persistent< ::capnp::Data,  ::sandstorm::ApiTokenOwner>::Server::dispatchCallInternal(methodId, context);
    case 0xdbb4d798ea67e2e7ull:
      return  ::sandstorm::UiView::Server::dispatchCallInternal(methodId, context);
    default:
      return internalUnimplemented("sandstorm/persistentuiview.capnp:PersistentUiView", interfaceId);
  }
}
::kj::Promise<void> PersistentUiView::Server::dispatchCallInternal(
    uint16_t methodId,
    ::capnp::CallContext< ::capnp::AnyPointer, ::capnp::AnyPointer> context) {
  switch (methodId) {
    default:
      (void)context;
      return ::capnp::Capability::Server::internalUnimplemented(
          "sandstorm/persistentuiview.capnp:PersistentUiView",
          0x826f7187e23c37c9ull, methodId);
  }
}
#endif  // !CAPNP_LITE

// PersistentUiView
#if !CAPNP_LITE
constexpr ::capnp::Kind PersistentUiView::_capnpPrivate::kind;
constexpr ::capnp::_::RawSchema const* PersistentUiView::_capnpPrivate::schema;
constexpr ::capnp::_::RawBrandedSchema const* PersistentUiView::_capnpPrivate::brand;
#endif  // !CAPNP_LITE


}  // namespace

